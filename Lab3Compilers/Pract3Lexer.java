// Generated from Pract3.g4 by ANTLR 4.5.1

  // import java packages
  import java.lang.String;
  import java.io.PrintStream;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class Pract3Lexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		WS=1, AT=2, LCB=3, RCB=4, LB=5, RB=6, Q=7, EQ=8, DASH=9, AND=10, BOOK=11, 
		ARTICLE=12, CONFERENCE=13, AUTHOR=14, TITLE=15, YEAR=16, PUBLISHER=17, 
		JOURNAL=18, PAGES=19, VOLUME=20, NUMBER=21, BOOKTITLE=22, MONTH=23, COMMA=24, 
		Jan=25, Feb=26, Mar=27, Apr=28, May=29, Jun=30, Jul=31, Aug=32, Sep=33, 
		Oct=34, Nov=35, Dec=36, DIGITS=37, PUNC=38, FREETEXT=39;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"WS", "AT", "LCB", "RCB", "LB", "RB", "Q", "EQ", "DASH", "AND", "BOOK", 
		"ARTICLE", "CONFERENCE", "AUTHOR", "TITLE", "YEAR", "PUBLISHER", "JOURNAL", 
		"PAGES", "VOLUME", "NUMBER", "BOOKTITLE", "MONTH", "COMMA", "Jan", "Feb", 
		"Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", 
		"DIGIT", "DIGITS", "CHAR", "PUNC", "FREETEXT"
	};

	private static final String[] _LITERAL_NAMES = {
		null, null, "'@'", "'{'", "'}'", "'('", "')'", "'\"'", "'='", "'-'", null, 
		"'book'", "'article'", "'inproceedings'", "'author'", "'title'", "'year'", 
		"'publisher'", "'journal'", "'pages'", "'volume'", "'number'", "'booktitle'", 
		"'month'", "','", "'Jan'", "'Feb'", "'Mar'", "'Apr'", "'May'", "'Jun'", 
		"'Jul'", "'Aug'", "'Sep'", "'Oct'", "'Nov'", "'Dec'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, "WS", "AT", "LCB", "RCB", "LB", "RB", "Q", "EQ", "DASH", "AND", 
		"BOOK", "ARTICLE", "CONFERENCE", "AUTHOR", "TITLE", "YEAR", "PUBLISHER", 
		"JOURNAL", "PAGES", "VOLUME", "NUMBER", "BOOKTITLE", "MONTH", "COMMA", 
		"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", 
		"Nov", "Dec", "DIGITS", "PUNC", "FREETEXT"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	  // EndNote XML print methods
	  public static void xmlout(String x, String y) { out.println("<"+x+">"+y+"</"+x+">");}
	  public static void xmlb(String x)             { out.println("<" +x+">");}
	  public static void xmle(String x)             { out.println("</"+x+">");}

	  // define your own members here
	  public static void recordb() {xmlb(RECORD);
	                                xmlout("rec-number", "" + records++);}
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

	  public static PrintStream out = System.out;
	  public static int records = 1;

	  public static final String RECORD = "record";
	  public static final String CONTRIBUTORS = "contributors";
	  public static final String AUTHORS = "authors";
	  public static final String AUTH = "author";
	  public static final String TITLES = "titles";
	  public static final String TIT = "title";
	  public static final String PAGE = "pages";


	public Pract3Lexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "Pract3.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2)\u0119\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\3\2\6\2"+
		"W\n\2\r\2\16\2X\3\2\3\2\3\3\3\3\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\b\3"+
		"\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\5\13s\n\13\3\f\3\f\3"+
		"\f\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\22\3"+
		"\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\22\3\23\3\23\3\23\3\23\3\23\3"+
		"\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3"+
		"\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\27\3"+
		"\27\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\30\3\30\3\31\3\31\3\32\3"+
		"\32\3\32\3\32\3\33\3\33\3\33\3\33\3\34\3\34\3\34\3\34\3\35\3\35\3\35\3"+
		"\35\3\36\3\36\3\36\3\36\3\37\3\37\3\37\3\37\3 \3 \3 \3 \3!\3!\3!\3!\3"+
		"\"\3\"\3\"\3\"\3#\3#\3#\3#\3$\3$\3$\3$\3%\3%\3%\3%\3&\3&\3\'\6\'\u010d"+
		"\n\'\r\'\16\'\u010e\3(\3(\3)\3)\3*\6*\u0116\n*\r*\16*\u0117\2\2+\3\3\5"+
		"\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21"+
		"!\22#\23%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\37= ?!"+
		"A\"C#E$G%I&K\2M\'O\2Q(S)\3\2\6\5\2\13\f\17\17\"\"\3\2\62;\5\2\62;C\\c"+
		"|\4\2\60\61<=\u011a\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2"+
		"\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3"+
		"\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2"+
		"\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2"+
		"\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2"+
		"\2\2\29\3\2\2\2\2;\3\2\2\2\2=\3\2\2\2\2?\3\2\2\2\2A\3\2\2\2\2C\3\2\2\2"+
		"\2E\3\2\2\2\2G\3\2\2\2\2I\3\2\2\2\2M\3\2\2\2\2Q\3\2\2\2\2S\3\2\2\2\3V"+
		"\3\2\2\2\5\\\3\2\2\2\7^\3\2\2\2\t`\3\2\2\2\13b\3\2\2\2\rd\3\2\2\2\17f"+
		"\3\2\2\2\21h\3\2\2\2\23j\3\2\2\2\25r\3\2\2\2\27t\3\2\2\2\31y\3\2\2\2\33"+
		"\u0081\3\2\2\2\35\u008f\3\2\2\2\37\u0096\3\2\2\2!\u009c\3\2\2\2#\u00a1"+
		"\3\2\2\2%\u00ab\3\2\2\2\'\u00b3\3\2\2\2)\u00b9\3\2\2\2+\u00c0\3\2\2\2"+
		"-\u00c7\3\2\2\2/\u00d1\3\2\2\2\61\u00d7\3\2\2\2\63\u00d9\3\2\2\2\65\u00dd"+
		"\3\2\2\2\67\u00e1\3\2\2\29\u00e5\3\2\2\2;\u00e9\3\2\2\2=\u00ed\3\2\2\2"+
		"?\u00f1\3\2\2\2A\u00f5\3\2\2\2C\u00f9\3\2\2\2E\u00fd\3\2\2\2G\u0101\3"+
		"\2\2\2I\u0105\3\2\2\2K\u0109\3\2\2\2M\u010c\3\2\2\2O\u0110\3\2\2\2Q\u0112"+
		"\3\2\2\2S\u0115\3\2\2\2UW\t\2\2\2VU\3\2\2\2WX\3\2\2\2XV\3\2\2\2XY\3\2"+
		"\2\2YZ\3\2\2\2Z[\b\2\2\2[\4\3\2\2\2\\]\7B\2\2]\6\3\2\2\2^_\7}\2\2_\b\3"+
		"\2\2\2`a\7\177\2\2a\n\3\2\2\2bc\7*\2\2c\f\3\2\2\2de\7+\2\2e\16\3\2\2\2"+
		"fg\7$\2\2g\20\3\2\2\2hi\7?\2\2i\22\3\2\2\2jk\7/\2\2k\24\3\2\2\2lm\7c\2"+
		"\2mn\7p\2\2ns\7f\2\2op\7C\2\2pq\7P\2\2qs\7F\2\2rl\3\2\2\2ro\3\2\2\2s\26"+
		"\3\2\2\2tu\7d\2\2uv\7q\2\2vw\7q\2\2wx\7m\2\2x\30\3\2\2\2yz\7c\2\2z{\7"+
		"t\2\2{|\7v\2\2|}\7k\2\2}~\7e\2\2~\177\7n\2\2\177\u0080\7g\2\2\u0080\32"+
		"\3\2\2\2\u0081\u0082\7k\2\2\u0082\u0083\7p\2\2\u0083\u0084\7r\2\2\u0084"+
		"\u0085\7t\2\2\u0085\u0086\7q\2\2\u0086\u0087\7e\2\2\u0087\u0088\7g\2\2"+
		"\u0088\u0089\7g\2\2\u0089\u008a\7f\2\2\u008a\u008b\7k\2\2\u008b\u008c"+
		"\7p\2\2\u008c\u008d\7i\2\2\u008d\u008e\7u\2\2\u008e\34\3\2\2\2\u008f\u0090"+
		"\7c\2\2\u0090\u0091\7w\2\2\u0091\u0092\7v\2\2\u0092\u0093\7j\2\2\u0093"+
		"\u0094\7q\2\2\u0094\u0095\7t\2\2\u0095\36\3\2\2\2\u0096\u0097\7v\2\2\u0097"+
		"\u0098\7k\2\2\u0098\u0099\7v\2\2\u0099\u009a\7n\2\2\u009a\u009b\7g\2\2"+
		"\u009b \3\2\2\2\u009c\u009d\7{\2\2\u009d\u009e\7g\2\2\u009e\u009f\7c\2"+
		"\2\u009f\u00a0\7t\2\2\u00a0\"\3\2\2\2\u00a1\u00a2\7r\2\2\u00a2\u00a3\7"+
		"w\2\2\u00a3\u00a4\7d\2\2\u00a4\u00a5\7n\2\2\u00a5\u00a6\7k\2\2\u00a6\u00a7"+
		"\7u\2\2\u00a7\u00a8\7j\2\2\u00a8\u00a9\7g\2\2\u00a9\u00aa\7t\2\2\u00aa"+
		"$\3\2\2\2\u00ab\u00ac\7l\2\2\u00ac\u00ad\7q\2\2\u00ad\u00ae\7w\2\2\u00ae"+
		"\u00af\7t\2\2\u00af\u00b0\7p\2\2\u00b0\u00b1\7c\2\2\u00b1\u00b2\7n\2\2"+
		"\u00b2&\3\2\2\2\u00b3\u00b4\7r\2\2\u00b4\u00b5\7c\2\2\u00b5\u00b6\7i\2"+
		"\2\u00b6\u00b7\7g\2\2\u00b7\u00b8\7u\2\2\u00b8(\3\2\2\2\u00b9\u00ba\7"+
		"x\2\2\u00ba\u00bb\7q\2\2\u00bb\u00bc\7n\2\2\u00bc\u00bd\7w\2\2\u00bd\u00be"+
		"\7o\2\2\u00be\u00bf\7g\2\2\u00bf*\3\2\2\2\u00c0\u00c1\7p\2\2\u00c1\u00c2"+
		"\7w\2\2\u00c2\u00c3\7o\2\2\u00c3\u00c4\7d\2\2\u00c4\u00c5\7g\2\2\u00c5"+
		"\u00c6\7t\2\2\u00c6,\3\2\2\2\u00c7\u00c8\7d\2\2\u00c8\u00c9\7q\2\2\u00c9"+
		"\u00ca\7q\2\2\u00ca\u00cb\7m\2\2\u00cb\u00cc\7v\2\2\u00cc\u00cd\7k\2\2"+
		"\u00cd\u00ce\7v\2\2\u00ce\u00cf\7n\2\2\u00cf\u00d0\7g\2\2\u00d0.\3\2\2"+
		"\2\u00d1\u00d2\7o\2\2\u00d2\u00d3\7q\2\2\u00d3\u00d4\7p\2\2\u00d4\u00d5"+
		"\7v\2\2\u00d5\u00d6\7j\2\2\u00d6\60\3\2\2\2\u00d7\u00d8\7.\2\2\u00d8\62"+
		"\3\2\2\2\u00d9\u00da\7L\2\2\u00da\u00db\7c\2\2\u00db\u00dc\7p\2\2\u00dc"+
		"\64\3\2\2\2\u00dd\u00de\7H\2\2\u00de\u00df\7g\2\2\u00df\u00e0\7d\2\2\u00e0"+
		"\66\3\2\2\2\u00e1\u00e2\7O\2\2\u00e2\u00e3\7c\2\2\u00e3\u00e4\7t\2\2\u00e4"+
		"8\3\2\2\2\u00e5\u00e6\7C\2\2\u00e6\u00e7\7r\2\2\u00e7\u00e8\7t\2\2\u00e8"+
		":\3\2\2\2\u00e9\u00ea\7O\2\2\u00ea\u00eb\7c\2\2\u00eb\u00ec\7{\2\2\u00ec"+
		"<\3\2\2\2\u00ed\u00ee\7L\2\2\u00ee\u00ef\7w\2\2\u00ef\u00f0\7p\2\2\u00f0"+
		">\3\2\2\2\u00f1\u00f2\7L\2\2\u00f2\u00f3\7w\2\2\u00f3\u00f4\7n\2\2\u00f4"+
		"@\3\2\2\2\u00f5\u00f6\7C\2\2\u00f6\u00f7\7w\2\2\u00f7\u00f8\7i\2\2\u00f8"+
		"B\3\2\2\2\u00f9\u00fa\7U\2\2\u00fa\u00fb\7g\2\2\u00fb\u00fc\7r\2\2\u00fc"+
		"D\3\2\2\2\u00fd\u00fe\7Q\2\2\u00fe\u00ff\7e\2\2\u00ff\u0100\7v\2\2\u0100"+
		"F\3\2\2\2\u0101\u0102\7P\2\2\u0102\u0103\7q\2\2\u0103\u0104\7x\2\2\u0104"+
		"H\3\2\2\2\u0105\u0106\7F\2\2\u0106\u0107\7g\2\2\u0107\u0108\7e\2\2\u0108"+
		"J\3\2\2\2\u0109\u010a\t\3\2\2\u010aL\3\2\2\2\u010b\u010d\5K&\2\u010c\u010b"+
		"\3\2\2\2\u010d\u010e\3\2\2\2\u010e\u010c\3\2\2\2\u010e\u010f\3\2\2\2\u010f"+
		"N\3\2\2\2\u0110\u0111\t\4\2\2\u0111P\3\2\2\2\u0112\u0113\t\5\2\2\u0113"+
		"R\3\2\2\2\u0114\u0116\5O(\2\u0115\u0114\3\2\2\2\u0116\u0117\3\2\2\2\u0117"+
		"\u0115\3\2\2\2\u0117\u0118\3\2\2\2\u0118T\3\2\2\2\7\2Xr\u010e\u0117\3"+
		"\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}