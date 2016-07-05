grammar Pract3;

@header {
  // import java packages
  import java.lang.String;
  import java.io.PrintStream;
  import java.util.Set;
  import java.util.HashSet;
  import java.util.Arrays;
}

@members{
  // EndNote XML print methods
  public static void xmlout(String x, String y) { out.println("<"+x+">"+y+"</"+x+">");}
  public static void xmlb(String x)             { out.println("<" +x+">");}
  public static void xmle(String x)             { out.println("</"+x+">");}

  // define your own members here
  public static void recordb() {
    lastParsedMonth = null;
    xmlb(RECORD);
    xmlout("rec-number", "" + records++);
  }
  public static void reftypeOut(String label, int type) {
    out.println("<ref-type name=\"" + label + "\">"+type+"</ref-type>");
  }

  public static String trim(String input, String character) {
    return trim(input, character, character);
  }
  // Trimmer end and begin if needed
  public static String trim(String input, String begin, String end) {
    if (input.startsWith(begin) && input.endsWith(end)) {
        return input.substring(1, input.length() - 1);
    }
    return input;
  }

  public static String stripString(String string) {
    return trim(trim(string, "\""), "{", "}");
  }

  public static PrintStream out = System.out;
  public static int records = 1;
  public static String lastParsedMonth = null;

  // Static strings
  public static final String RECORD = "record";
  public static final String CONTRIBUTORS = "contributors";
  public static final String AUTHORS = "authors";
  public static final String AUTH = "author";
  public static final String TITLES = "titles";
  public static final String TIT = "title";
  public static final String PAGE = "pages";
  public static final String PUB = "publisher";
  public static final String DATES = "dates";
  public static final String YEAR_EL = "year";
  public static final String MONTH_EL = "month";
  public static final String PERIODICAL = "periodical";
  public static final String FULLTITLE = "full-title";
  public static final String VOL = "volume";
  public static final String NUM = "number";

  public static final Set<String> MONTHS = new HashSet<String>();

  static {
    String[] months = {"January", "Jan","Februari", "Feb","March", "Mar","April", "Apr","May", "May","June", "Jun","July", "Jul","August", "Aug","September", "Sep","October", "Oct","November", "Nov","December", "Dec"};
    for (String m : months) {
      MONTHS.add(m);
    }
  }
}

// implement your LL(1) grammar
// parser should skip 'unknown' bibtex entries, i.e. don't generate EndNote output
//										'unknown' fields

bibtex
  @init{System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<xml>\r\n<records>");}
  @after{System.out.println("</records>\r\n</xml>");}
  : s
  ;

s : b EOF;
b : b bb
  | ;
bb
  : AT
  (validCase
  | other);

validCase
  @init{recordb();}
  @after{xmle(RECORD);}
: book | article | conference;
other : label otherBody;

otherBody : LCB obody RCB
          | LB obody RB;
obody : label otherField+;

article : ARTICLE articleBody;

articleBody : LCB abody RCB
         | LB abody RB;
abody
  : label {reftypeOut($label.text, 17);}
articlefield+;
articlefield : sep (authorfield
             | titlefield
             | journalfield
             | pagesfield
             | volumefield
             | numberfield
             | yearfield
             | otherField);

conference : CONFERENCE conferenceBody;
conferenceBody : LCB cbody RCB
               | LB cbody RB;
cbody : label   {reftypeOut($label.text, 47);}
        cfields+;
cfields : sep (authorfield
        | titlefield
        | booktitlefield
        | yearfield
        | monthfield
        | pagesfield
        | otherField);

book : BOOK bookBody;
bookBody : LCB bbody RCB
         | LB bbody RB;

bbody : label  {reftypeOut($label.text, 6);}
        bookfield+;
bookfield : sep (authorfield
         | titlefield
         | yearfield
         | publisherfield
         | otherField);

authorfield: AUTHOR EQ author;
yearfield : YEAR EQ qint
{
  String year = stripString($qint.text);
  xmlb(DATES);
  xmlout(YEAR_EL, year);
  if (lastParsedMonth != null) {
    xmlout(MONTH_EL, lastParsedMonth);
    lastParsedMonth = null;
  }
  xmle(DATES);
};
monthfield : MONTH EQ monthString
{
  lastParsedMonth = stripString($monthString.text);
};
titlefield : TITLE EQ string
{
    xmlb(TITLES);
    xmlout(TIT, $string.text.substring(1, $string.text.length() - 1));
    xmle(TITLES);
}
;
publisherfield : PUBLISHER EQ string
{
    xmlout(PUB, stripString($string.text));
}
;
journalfield : JOURNAL EQ string
{
  xmlb(PERIODICAL);
  xmlout(FULLTITLE, stripString($string.text));
  xmle(PERIODICAL);
}
;
pagesfield : PAGES EQ pagesString {xmlout(PAGE, stripString($pagesString.text));};
booktitlefield : BOOKTITLE EQ string;
volumefield : VOLUME EQ qint {xmlout(VOL, stripString($qint.text));};
numberfield : NUMBER EQ qint {xmlout(NUM, stripString($qint.text));};
otherField : free EQ otherRH;
otherRH : LCB value RCB
        | Q value Q
        | value;
value : (free | DIGITS)*;

author : LCB authorlist RCB
       | Q authorlist Q;
authorlist
@init{
    xmlb(CONTRIBUTORS);
    xmlb(AUTHORS);
}
@after{
    xmle(AUTHORS);
    xmle(CONTRIBUTORS);
}
: free (AND free)*
{
    for (String s : $free.text.split("and|AND")) {
        xmlout(AUTH, s.trim());
    }
};
string : Q free Q
       | LCB free RCB;
pagesString : Q pages Q
            | LCB pages RCB;
pages : firstPage=DIGITS DASH DASH lastPage=DIGITS
        {
            if ($lastPage.int < $firstPage.int) {
               System.err.println("Last page comes before first page at line " + $firstPage.line + ", stop parsing");
               System.exit(0);
            }
        };

label : FREETEXT;
sep : COMMA ;

qint : DIGITS
     | Q DIGITS Q
     | LCB DIGITS RCB;
monthString : Q month Q
            | LCB month RCB;
month : DIGITS {
        if ($DIGITS.int < 1 || $DIGITS.int > 12) {
          System.err.println("Invalid month at line " + $DIGITS.line + ", stop parsing.");
          System.exit(0);
        }
      }
      | FREETEXT {
        if (!MONTHS.contains($FREETEXT.text)) {
          System.err.println("Invalid month at line " + $FREETEXT.line + ", stop parsing.");
          System.exit(0);
        }
      };
free : (FREETEXT | COMMA | PUNC | DASH | keyword)+;
keyword : AND | BOOK | ARTICLE | CONFERENCE | AUTHOR| TITLE | YEAR | PUBLISHER | JOURNAL | PAGES | VOLUME | NUMBER | BOOKTITLE | MONTH | COMMA;


// Lexer rules
WS : (' '|'\t'|'\n'|'\r')+  -> channel(HIDDEN) ;  // ignores spaces
AT : '@';
LCB : '{';
RCB : '}';
LB : '(';
RB : ')';
Q : '"';
EQ : '=';
DASH : '-';
AND : 'and' | 'AND';
BOOK : 'book';
ARTICLE : 'article';
CONFERENCE : 'inproceedings';
AUTHOR: 'author';
TITLE : 'title';
YEAR : 'year';
PUBLISHER : 'publisher';
JOURNAL : 'journal';
PAGES : 'pages';
VOLUME : 'volume';
NUMBER : 'number';
BOOKTITLE : 'booktitle';
MONTH : 'month';
COMMA : ',';
fragment DIGIT : [0-9];
DIGITS : DIGIT+;
fragment CHAR : [a-zA-Z0-9];
PUNC : [.:;/];
FREETEXT : CHAR+;


