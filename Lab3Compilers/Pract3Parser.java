// Generated from Pract3.g4 by ANTLR 4.5.1

  // import java packages
  import java.lang.String;
  import java.io.PrintStream;

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class Pract3Parser extends Parser {
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
	public static final int
		RULE_bibtex = 0, RULE_s = 1, RULE_b = 2, RULE_bb = 3, RULE_validCase = 4, 
		RULE_other = 5, RULE_otherBody = 6, RULE_obody = 7, RULE_article = 8, 
		RULE_articleBody = 9, RULE_abody = 10, RULE_articlefield = 11, RULE_conference = 12, 
		RULE_conferenceBody = 13, RULE_cbody = 14, RULE_cfields = 15, RULE_book = 16, 
		RULE_bookBody = 17, RULE_bbody = 18, RULE_bookfield = 19, RULE_authorfield = 20, 
		RULE_yearfield = 21, RULE_monthfield = 22, RULE_titlefield = 23, RULE_publisherfield = 24, 
		RULE_journalfield = 25, RULE_pagesfield = 26, RULE_booktitlefield = 27, 
		RULE_volumefield = 28, RULE_numberfield = 29, RULE_otherField = 30, RULE_otherRH = 31, 
		RULE_value = 32, RULE_author = 33, RULE_authorlist = 34, RULE_string = 35, 
		RULE_pagesString = 36, RULE_pagess = 37, RULE_label = 38, RULE_sep = 39, 
		RULE_qint = 40, RULE_monthString = 41, RULE_month = 42, RULE_free = 43, 
		RULE_keyword = 44;
	public static final String[] ruleNames = {
		"bibtex", "s", "b", "bb", "validCase", "other", "otherBody", "obody", 
		"article", "articleBody", "abody", "articlefield", "conference", "conferenceBody", 
		"cbody", "cfields", "book", "bookBody", "bbody", "bookfield", "authorfield", 
		"yearfield", "monthfield", "titlefield", "publisherfield", "journalfield", 
		"pagesfield", "booktitlefield", "volumefield", "numberfield", "otherField", 
		"otherRH", "value", "author", "authorlist", "string", "pagesString", "pagess", 
		"label", "sep", "qint", "monthString", "month", "free", "keyword"
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

	@Override
	public String getGrammarFileName() { return "Pract3.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }


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

	public Pract3Parser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class BibtexContext extends ParserRuleContext {
		public SContext s() {
			return getRuleContext(SContext.class,0);
		}
		public BibtexContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bibtex; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterBibtex(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitBibtex(this);
		}
	}

	public final BibtexContext bibtex() throws RecognitionException {
		BibtexContext _localctx = new BibtexContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_bibtex);
		System.out.println("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n<xml>\r\n<records>");
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(90);
			s();
			}
			System.out.println("</records>\r\n</xml>");
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SContext extends ParserRuleContext {
		public BContext b() {
			return getRuleContext(BContext.class,0);
		}
		public TerminalNode EOF() { return getToken(Pract3Parser.EOF, 0); }
		public SContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_s; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterS(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitS(this);
		}
	}

	public final SContext s() throws RecognitionException {
		SContext _localctx = new SContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_s);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			b(0);
			setState(93);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BContext extends ParserRuleContext {
		public BContext b() {
			return getRuleContext(BContext.class,0);
		}
		public BbContext bb() {
			return getRuleContext(BbContext.class,0);
		}
		public BContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_b; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterB(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitB(this);
		}
	}

	public final BContext b() throws RecognitionException {
		return b(0);
	}

	private BContext b(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		BContext _localctx = new BContext(_ctx, _parentState);
		BContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, 4, RULE_b, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			{
			}
			_ctx.stop = _input.LT(-1);
			setState(100);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					{
					_localctx = new BContext(_parentctx, _parentState);
					pushNewRecursionContext(_localctx, _startState, RULE_b);
					setState(96);
					if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
					setState(97);
					bb();
					}
					} 
				}
				setState(102);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,0,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class BbContext extends ParserRuleContext {
		public TerminalNode AT() { return getToken(Pract3Parser.AT, 0); }
		public ValidCaseContext validCase() {
			return getRuleContext(ValidCaseContext.class,0);
		}
		public OtherContext other() {
			return getRuleContext(OtherContext.class,0);
		}
		public BbContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bb; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterBb(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitBb(this);
		}
	}

	public final BbContext bb() throws RecognitionException {
		BbContext _localctx = new BbContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_bb);
		recordb();
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(103);
			match(AT);
			setState(106);
			switch (_input.LA(1)) {
			case BOOK:
			case ARTICLE:
			case CONFERENCE:
				{
				setState(104);
				validCase();
				}
				break;
			case FREETEXT:
				{
				setState(105);
				other();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			}
			xmle(RECORD);
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValidCaseContext extends ParserRuleContext {
		public BookContext book() {
			return getRuleContext(BookContext.class,0);
		}
		public ArticleContext article() {
			return getRuleContext(ArticleContext.class,0);
		}
		public ConferenceContext conference() {
			return getRuleContext(ConferenceContext.class,0);
		}
		public ValidCaseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_validCase; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterValidCase(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitValidCase(this);
		}
	}

	public final ValidCaseContext validCase() throws RecognitionException {
		ValidCaseContext _localctx = new ValidCaseContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_validCase);
		try {
			setState(111);
			switch (_input.LA(1)) {
			case BOOK:
				enterOuterAlt(_localctx, 1);
				{
				setState(108);
				book();
				}
				break;
			case ARTICLE:
				enterOuterAlt(_localctx, 2);
				{
				setState(109);
				article();
				}
				break;
			case CONFERENCE:
				enterOuterAlt(_localctx, 3);
				{
				setState(110);
				conference();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OtherContext extends ParserRuleContext {
		public LabelContext label() {
			return getRuleContext(LabelContext.class,0);
		}
		public OtherBodyContext otherBody() {
			return getRuleContext(OtherBodyContext.class,0);
		}
		public OtherContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_other; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterOther(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitOther(this);
		}
	}

	public final OtherContext other() throws RecognitionException {
		OtherContext _localctx = new OtherContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_other);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			label();
			setState(114);
			otherBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OtherBodyContext extends ParserRuleContext {
		public TerminalNode LCB() { return getToken(Pract3Parser.LCB, 0); }
		public ObodyContext obody() {
			return getRuleContext(ObodyContext.class,0);
		}
		public TerminalNode RCB() { return getToken(Pract3Parser.RCB, 0); }
		public TerminalNode LB() { return getToken(Pract3Parser.LB, 0); }
		public TerminalNode RB() { return getToken(Pract3Parser.RB, 0); }
		public OtherBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_otherBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterOtherBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitOtherBody(this);
		}
	}

	public final OtherBodyContext otherBody() throws RecognitionException {
		OtherBodyContext _localctx = new OtherBodyContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_otherBody);
		try {
			setState(124);
			switch (_input.LA(1)) {
			case LCB:
				enterOuterAlt(_localctx, 1);
				{
				setState(116);
				match(LCB);
				setState(117);
				obody();
				setState(118);
				match(RCB);
				}
				break;
			case LB:
				enterOuterAlt(_localctx, 2);
				{
				setState(120);
				match(LB);
				setState(121);
				obody();
				setState(122);
				match(RB);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ObodyContext extends ParserRuleContext {
		public LabelContext label() {
			return getRuleContext(LabelContext.class,0);
		}
		public List<OtherFieldContext> otherField() {
			return getRuleContexts(OtherFieldContext.class);
		}
		public OtherFieldContext otherField(int i) {
			return getRuleContext(OtherFieldContext.class,i);
		}
		public ObodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_obody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterObody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitObody(this);
		}
	}

	public final ObodyContext obody() throws RecognitionException {
		ObodyContext _localctx = new ObodyContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_obody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			label();
			setState(128); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(127);
				otherField();
				}
				}
				setState(130); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( (((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << DASH) | (1L << AND) | (1L << BOOK) | (1L << ARTICLE) | (1L << CONFERENCE) | (1L << AUTHOR) | (1L << TITLE) | (1L << YEAR) | (1L << PUBLISHER) | (1L << JOURNAL) | (1L << PAGES) | (1L << VOLUME) | (1L << NUMBER) | (1L << BOOKTITLE) | (1L << MONTH) | (1L << COMMA) | (1L << Jan) | (1L << Feb) | (1L << Mar) | (1L << Apr) | (1L << May) | (1L << Jun) | (1L << Jul) | (1L << Aug) | (1L << Sep) | (1L << Oct) | (1L << Nov) | (1L << Dec) | (1L << PUNC) | (1L << FREETEXT))) != 0) );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArticleContext extends ParserRuleContext {
		public TerminalNode ARTICLE() { return getToken(Pract3Parser.ARTICLE, 0); }
		public ArticleBodyContext articleBody() {
			return getRuleContext(ArticleBodyContext.class,0);
		}
		public ArticleContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_article; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterArticle(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitArticle(this);
		}
	}

	public final ArticleContext article() throws RecognitionException {
		ArticleContext _localctx = new ArticleContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_article);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			match(ARTICLE);
			setState(133);
			articleBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArticleBodyContext extends ParserRuleContext {
		public TerminalNode LCB() { return getToken(Pract3Parser.LCB, 0); }
		public AbodyContext abody() {
			return getRuleContext(AbodyContext.class,0);
		}
		public TerminalNode RCB() { return getToken(Pract3Parser.RCB, 0); }
		public TerminalNode LB() { return getToken(Pract3Parser.LB, 0); }
		public TerminalNode RB() { return getToken(Pract3Parser.RB, 0); }
		public ArticleBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_articleBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterArticleBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitArticleBody(this);
		}
	}

	public final ArticleBodyContext articleBody() throws RecognitionException {
		ArticleBodyContext _localctx = new ArticleBodyContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_articleBody);
		try {
			setState(143);
			switch (_input.LA(1)) {
			case LCB:
				enterOuterAlt(_localctx, 1);
				{
				setState(135);
				match(LCB);
				setState(136);
				abody();
				setState(137);
				match(RCB);
				}
				break;
			case LB:
				enterOuterAlt(_localctx, 2);
				{
				setState(139);
				match(LB);
				setState(140);
				abody();
				setState(141);
				match(RB);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AbodyContext extends ParserRuleContext {
		public LabelContext label;
		public LabelContext label() {
			return getRuleContext(LabelContext.class,0);
		}
		public List<ArticlefieldContext> articlefield() {
			return getRuleContexts(ArticlefieldContext.class);
		}
		public ArticlefieldContext articlefield(int i) {
			return getRuleContext(ArticlefieldContext.class,i);
		}
		public AbodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_abody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterAbody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitAbody(this);
		}
	}

	public final AbodyContext abody() throws RecognitionException {
		AbodyContext _localctx = new AbodyContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_abody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(145);
			((AbodyContext)_localctx).label = label();
			reftypeOut((((AbodyContext)_localctx).label!=null?_input.getText(((AbodyContext)_localctx).label.start,((AbodyContext)_localctx).label.stop):null), 17);
			setState(148); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(147);
				articlefield();
				}
				}
				setState(150); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==COMMA );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArticlefieldContext extends ParserRuleContext {
		public SepContext sep() {
			return getRuleContext(SepContext.class,0);
		}
		public AuthorfieldContext authorfield() {
			return getRuleContext(AuthorfieldContext.class,0);
		}
		public TitlefieldContext titlefield() {
			return getRuleContext(TitlefieldContext.class,0);
		}
		public JournalfieldContext journalfield() {
			return getRuleContext(JournalfieldContext.class,0);
		}
		public PagesfieldContext pagesfield() {
			return getRuleContext(PagesfieldContext.class,0);
		}
		public VolumefieldContext volumefield() {
			return getRuleContext(VolumefieldContext.class,0);
		}
		public NumberfieldContext numberfield() {
			return getRuleContext(NumberfieldContext.class,0);
		}
		public YearfieldContext yearfield() {
			return getRuleContext(YearfieldContext.class,0);
		}
		public OtherFieldContext otherField() {
			return getRuleContext(OtherFieldContext.class,0);
		}
		public ArticlefieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_articlefield; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterArticlefield(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitArticlefield(this);
		}
	}

	public final ArticlefieldContext articlefield() throws RecognitionException {
		ArticlefieldContext _localctx = new ArticlefieldContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_articlefield);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(152);
			sep();
			setState(161);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(153);
				authorfield();
				}
				break;
			case 2:
				{
				setState(154);
				titlefield();
				}
				break;
			case 3:
				{
				setState(155);
				journalfield();
				}
				break;
			case 4:
				{
				setState(156);
				pagesfield();
				}
				break;
			case 5:
				{
				setState(157);
				volumefield();
				}
				break;
			case 6:
				{
				setState(158);
				numberfield();
				}
				break;
			case 7:
				{
				setState(159);
				yearfield();
				}
				break;
			case 8:
				{
				setState(160);
				otherField();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConferenceContext extends ParserRuleContext {
		public TerminalNode CONFERENCE() { return getToken(Pract3Parser.CONFERENCE, 0); }
		public ConferenceBodyContext conferenceBody() {
			return getRuleContext(ConferenceBodyContext.class,0);
		}
		public ConferenceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conference; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterConference(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitConference(this);
		}
	}

	public final ConferenceContext conference() throws RecognitionException {
		ConferenceContext _localctx = new ConferenceContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_conference);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(163);
			match(CONFERENCE);
			setState(164);
			conferenceBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ConferenceBodyContext extends ParserRuleContext {
		public TerminalNode LCB() { return getToken(Pract3Parser.LCB, 0); }
		public CbodyContext cbody() {
			return getRuleContext(CbodyContext.class,0);
		}
		public TerminalNode RCB() { return getToken(Pract3Parser.RCB, 0); }
		public TerminalNode LB() { return getToken(Pract3Parser.LB, 0); }
		public TerminalNode RB() { return getToken(Pract3Parser.RB, 0); }
		public ConferenceBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_conferenceBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterConferenceBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitConferenceBody(this);
		}
	}

	public final ConferenceBodyContext conferenceBody() throws RecognitionException {
		ConferenceBodyContext _localctx = new ConferenceBodyContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_conferenceBody);
		try {
			setState(174);
			switch (_input.LA(1)) {
			case LCB:
				enterOuterAlt(_localctx, 1);
				{
				setState(166);
				match(LCB);
				setState(167);
				cbody();
				setState(168);
				match(RCB);
				}
				break;
			case LB:
				enterOuterAlt(_localctx, 2);
				{
				setState(170);
				match(LB);
				setState(171);
				cbody();
				setState(172);
				match(RB);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CbodyContext extends ParserRuleContext {
		public LabelContext label;
		public LabelContext label() {
			return getRuleContext(LabelContext.class,0);
		}
		public List<CfieldsContext> cfields() {
			return getRuleContexts(CfieldsContext.class);
		}
		public CfieldsContext cfields(int i) {
			return getRuleContext(CfieldsContext.class,i);
		}
		public CbodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cbody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterCbody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitCbody(this);
		}
	}

	public final CbodyContext cbody() throws RecognitionException {
		CbodyContext _localctx = new CbodyContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_cbody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(176);
			((CbodyContext)_localctx).label = label();
			reftypeOut((((CbodyContext)_localctx).label!=null?_input.getText(((CbodyContext)_localctx).label.start,((CbodyContext)_localctx).label.stop):null), 47);
			setState(179); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(178);
				cfields();
				}
				}
				setState(181); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==COMMA );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class CfieldsContext extends ParserRuleContext {
		public SepContext sep() {
			return getRuleContext(SepContext.class,0);
		}
		public AuthorfieldContext authorfield() {
			return getRuleContext(AuthorfieldContext.class,0);
		}
		public TitlefieldContext titlefield() {
			return getRuleContext(TitlefieldContext.class,0);
		}
		public BooktitlefieldContext booktitlefield() {
			return getRuleContext(BooktitlefieldContext.class,0);
		}
		public YearfieldContext yearfield() {
			return getRuleContext(YearfieldContext.class,0);
		}
		public MonthfieldContext monthfield() {
			return getRuleContext(MonthfieldContext.class,0);
		}
		public PagesfieldContext pagesfield() {
			return getRuleContext(PagesfieldContext.class,0);
		}
		public OtherFieldContext otherField() {
			return getRuleContext(OtherFieldContext.class,0);
		}
		public CfieldsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cfields; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterCfields(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitCfields(this);
		}
	}

	public final CfieldsContext cfields() throws RecognitionException {
		CfieldsContext _localctx = new CfieldsContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_cfields);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(183);
			sep();
			setState(191);
			switch ( getInterpreter().adaptivePredict(_input,10,_ctx) ) {
			case 1:
				{
				setState(184);
				authorfield();
				}
				break;
			case 2:
				{
				setState(185);
				titlefield();
				}
				break;
			case 3:
				{
				setState(186);
				booktitlefield();
				}
				break;
			case 4:
				{
				setState(187);
				yearfield();
				}
				break;
			case 5:
				{
				setState(188);
				monthfield();
				}
				break;
			case 6:
				{
				setState(189);
				pagesfield();
				}
				break;
			case 7:
				{
				setState(190);
				otherField();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BookContext extends ParserRuleContext {
		public TerminalNode BOOK() { return getToken(Pract3Parser.BOOK, 0); }
		public BookBodyContext bookBody() {
			return getRuleContext(BookBodyContext.class,0);
		}
		public BookContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_book; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterBook(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitBook(this);
		}
	}

	public final BookContext book() throws RecognitionException {
		BookContext _localctx = new BookContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_book);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(193);
			match(BOOK);
			setState(194);
			bookBody();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BookBodyContext extends ParserRuleContext {
		public TerminalNode LCB() { return getToken(Pract3Parser.LCB, 0); }
		public BbodyContext bbody() {
			return getRuleContext(BbodyContext.class,0);
		}
		public TerminalNode RCB() { return getToken(Pract3Parser.RCB, 0); }
		public TerminalNode LB() { return getToken(Pract3Parser.LB, 0); }
		public TerminalNode RB() { return getToken(Pract3Parser.RB, 0); }
		public BookBodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bookBody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterBookBody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitBookBody(this);
		}
	}

	public final BookBodyContext bookBody() throws RecognitionException {
		BookBodyContext _localctx = new BookBodyContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_bookBody);
		try {
			setState(204);
			switch (_input.LA(1)) {
			case LCB:
				enterOuterAlt(_localctx, 1);
				{
				setState(196);
				match(LCB);
				setState(197);
				bbody();
				setState(198);
				match(RCB);
				}
				break;
			case LB:
				enterOuterAlt(_localctx, 2);
				{
				setState(200);
				match(LB);
				setState(201);
				bbody();
				setState(202);
				match(RB);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BbodyContext extends ParserRuleContext {
		public LabelContext label;
		public LabelContext label() {
			return getRuleContext(LabelContext.class,0);
		}
		public List<BookfieldContext> bookfield() {
			return getRuleContexts(BookfieldContext.class);
		}
		public BookfieldContext bookfield(int i) {
			return getRuleContext(BookfieldContext.class,i);
		}
		public BbodyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bbody; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterBbody(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitBbody(this);
		}
	}

	public final BbodyContext bbody() throws RecognitionException {
		BbodyContext _localctx = new BbodyContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_bbody);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(206);
			((BbodyContext)_localctx).label = label();
			reftypeOut((((BbodyContext)_localctx).label!=null?_input.getText(((BbodyContext)_localctx).label.start,((BbodyContext)_localctx).label.stop):null), 6);
			setState(209); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				{
				setState(208);
				bookfield();
				}
				}
				setState(211); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==COMMA );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BookfieldContext extends ParserRuleContext {
		public SepContext sep() {
			return getRuleContext(SepContext.class,0);
		}
		public AuthorfieldContext authorfield() {
			return getRuleContext(AuthorfieldContext.class,0);
		}
		public TitlefieldContext titlefield() {
			return getRuleContext(TitlefieldContext.class,0);
		}
		public YearfieldContext yearfield() {
			return getRuleContext(YearfieldContext.class,0);
		}
		public PublisherfieldContext publisherfield() {
			return getRuleContext(PublisherfieldContext.class,0);
		}
		public OtherFieldContext otherField() {
			return getRuleContext(OtherFieldContext.class,0);
		}
		public BookfieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bookfield; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterBookfield(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitBookfield(this);
		}
	}

	public final BookfieldContext bookfield() throws RecognitionException {
		BookfieldContext _localctx = new BookfieldContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_bookfield);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(213);
			sep();
			setState(219);
			switch ( getInterpreter().adaptivePredict(_input,13,_ctx) ) {
			case 1:
				{
				setState(214);
				authorfield();
				}
				break;
			case 2:
				{
				setState(215);
				titlefield();
				}
				break;
			case 3:
				{
				setState(216);
				yearfield();
				}
				break;
			case 4:
				{
				setState(217);
				publisherfield();
				}
				break;
			case 5:
				{
				setState(218);
				otherField();
				}
				break;
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AuthorfieldContext extends ParserRuleContext {
		public TerminalNode AUTHOR() { return getToken(Pract3Parser.AUTHOR, 0); }
		public TerminalNode EQ() { return getToken(Pract3Parser.EQ, 0); }
		public AuthorContext author() {
			return getRuleContext(AuthorContext.class,0);
		}
		public AuthorfieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_authorfield; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterAuthorfield(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitAuthorfield(this);
		}
	}

	public final AuthorfieldContext authorfield() throws RecognitionException {
		AuthorfieldContext _localctx = new AuthorfieldContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_authorfield);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(221);
			match(AUTHOR);
			setState(222);
			match(EQ);
			setState(223);
			author();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class YearfieldContext extends ParserRuleContext {
		public TerminalNode YEAR() { return getToken(Pract3Parser.YEAR, 0); }
		public TerminalNode EQ() { return getToken(Pract3Parser.EQ, 0); }
		public QintContext qint() {
			return getRuleContext(QintContext.class,0);
		}
		public YearfieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_yearfield; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterYearfield(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitYearfield(this);
		}
	}

	public final YearfieldContext yearfield() throws RecognitionException {
		YearfieldContext _localctx = new YearfieldContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_yearfield);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(225);
			match(YEAR);
			setState(226);
			match(EQ);
			setState(227);
			qint();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MonthfieldContext extends ParserRuleContext {
		public TerminalNode MONTH() { return getToken(Pract3Parser.MONTH, 0); }
		public TerminalNode EQ() { return getToken(Pract3Parser.EQ, 0); }
		public MonthStringContext monthString() {
			return getRuleContext(MonthStringContext.class,0);
		}
		public MonthfieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_monthfield; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterMonthfield(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitMonthfield(this);
		}
	}

	public final MonthfieldContext monthfield() throws RecognitionException {
		MonthfieldContext _localctx = new MonthfieldContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_monthfield);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(229);
			match(MONTH);
			setState(230);
			match(EQ);
			setState(231);
			monthString();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TitlefieldContext extends ParserRuleContext {
		public StringContext string;
		public TerminalNode TITLE() { return getToken(Pract3Parser.TITLE, 0); }
		public TerminalNode EQ() { return getToken(Pract3Parser.EQ, 0); }
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public TitlefieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_titlefield; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterTitlefield(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitTitlefield(this);
		}
	}

	public final TitlefieldContext titlefield() throws RecognitionException {
		TitlefieldContext _localctx = new TitlefieldContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_titlefield);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(233);
			match(TITLE);
			setState(234);
			match(EQ);
			setState(235);
			((TitlefieldContext)_localctx).string = string();

			    xmlb(TITLES);
			    xmlout(TIT, (((TitlefieldContext)_localctx).string!=null?_input.getText(((TitlefieldContext)_localctx).string.start,((TitlefieldContext)_localctx).string.stop):null).substring(1, (((TitlefieldContext)_localctx).string!=null?_input.getText(((TitlefieldContext)_localctx).string.start,((TitlefieldContext)_localctx).string.stop):null).length() - 1));
			    xmle(TITLES);

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PublisherfieldContext extends ParserRuleContext {
		public TerminalNode PUBLISHER() { return getToken(Pract3Parser.PUBLISHER, 0); }
		public TerminalNode EQ() { return getToken(Pract3Parser.EQ, 0); }
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public PublisherfieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_publisherfield; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterPublisherfield(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitPublisherfield(this);
		}
	}

	public final PublisherfieldContext publisherfield() throws RecognitionException {
		PublisherfieldContext _localctx = new PublisherfieldContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_publisherfield);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(238);
			match(PUBLISHER);
			setState(239);
			match(EQ);
			setState(240);
			string();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class JournalfieldContext extends ParserRuleContext {
		public TerminalNode JOURNAL() { return getToken(Pract3Parser.JOURNAL, 0); }
		public TerminalNode EQ() { return getToken(Pract3Parser.EQ, 0); }
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public JournalfieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_journalfield; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterJournalfield(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitJournalfield(this);
		}
	}

	public final JournalfieldContext journalfield() throws RecognitionException {
		JournalfieldContext _localctx = new JournalfieldContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_journalfield);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(242);
			match(JOURNAL);
			setState(243);
			match(EQ);
			setState(244);
			string();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PagesfieldContext extends ParserRuleContext {
		public PagesStringContext pagesString;
		public TerminalNode PAGES() { return getToken(Pract3Parser.PAGES, 0); }
		public TerminalNode EQ() { return getToken(Pract3Parser.EQ, 0); }
		public PagesStringContext pagesString() {
			return getRuleContext(PagesStringContext.class,0);
		}
		public PagesfieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pagesfield; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterPagesfield(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitPagesfield(this);
		}
	}

	public final PagesfieldContext pagesfield() throws RecognitionException {
		PagesfieldContext _localctx = new PagesfieldContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_pagesfield);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			match(PAGES);
			setState(247);
			match(EQ);
			setState(248);
			((PagesfieldContext)_localctx).pagesString = pagesString();
			xmlout(PAGE, trim(trim((((PagesfieldContext)_localctx).pagesString!=null?_input.getText(((PagesfieldContext)_localctx).pagesString.start,((PagesfieldContext)_localctx).pagesString.stop):null), "\""), "{", "}"));
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BooktitlefieldContext extends ParserRuleContext {
		public TerminalNode BOOKTITLE() { return getToken(Pract3Parser.BOOKTITLE, 0); }
		public TerminalNode EQ() { return getToken(Pract3Parser.EQ, 0); }
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public BooktitlefieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_booktitlefield; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterBooktitlefield(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitBooktitlefield(this);
		}
	}

	public final BooktitlefieldContext booktitlefield() throws RecognitionException {
		BooktitlefieldContext _localctx = new BooktitlefieldContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_booktitlefield);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(251);
			match(BOOKTITLE);
			setState(252);
			match(EQ);
			setState(253);
			string();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class VolumefieldContext extends ParserRuleContext {
		public TerminalNode VOLUME() { return getToken(Pract3Parser.VOLUME, 0); }
		public TerminalNode EQ() { return getToken(Pract3Parser.EQ, 0); }
		public QintContext qint() {
			return getRuleContext(QintContext.class,0);
		}
		public VolumefieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_volumefield; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterVolumefield(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitVolumefield(this);
		}
	}

	public final VolumefieldContext volumefield() throws RecognitionException {
		VolumefieldContext _localctx = new VolumefieldContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_volumefield);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(255);
			match(VOLUME);
			setState(256);
			match(EQ);
			setState(257);
			qint();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumberfieldContext extends ParserRuleContext {
		public TerminalNode NUMBER() { return getToken(Pract3Parser.NUMBER, 0); }
		public TerminalNode EQ() { return getToken(Pract3Parser.EQ, 0); }
		public QintContext qint() {
			return getRuleContext(QintContext.class,0);
		}
		public NumberfieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_numberfield; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterNumberfield(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitNumberfield(this);
		}
	}

	public final NumberfieldContext numberfield() throws RecognitionException {
		NumberfieldContext _localctx = new NumberfieldContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_numberfield);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(259);
			match(NUMBER);
			setState(260);
			match(EQ);
			setState(261);
			qint();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OtherFieldContext extends ParserRuleContext {
		public FreeContext free() {
			return getRuleContext(FreeContext.class,0);
		}
		public TerminalNode EQ() { return getToken(Pract3Parser.EQ, 0); }
		public OtherRHContext otherRH() {
			return getRuleContext(OtherRHContext.class,0);
		}
		public OtherFieldContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_otherField; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterOtherField(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitOtherField(this);
		}
	}

	public final OtherFieldContext otherField() throws RecognitionException {
		OtherFieldContext _localctx = new OtherFieldContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_otherField);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(263);
			free();
			setState(264);
			match(EQ);
			setState(265);
			otherRH();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class OtherRHContext extends ParserRuleContext {
		public TerminalNode LCB() { return getToken(Pract3Parser.LCB, 0); }
		public ValueContext value() {
			return getRuleContext(ValueContext.class,0);
		}
		public TerminalNode RCB() { return getToken(Pract3Parser.RCB, 0); }
		public List<TerminalNode> Q() { return getTokens(Pract3Parser.Q); }
		public TerminalNode Q(int i) {
			return getToken(Pract3Parser.Q, i);
		}
		public OtherRHContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_otherRH; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterOtherRH(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitOtherRH(this);
		}
	}

	public final OtherRHContext otherRH() throws RecognitionException {
		OtherRHContext _localctx = new OtherRHContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_otherRH);
		try {
			setState(276);
			switch (_input.LA(1)) {
			case LCB:
				enterOuterAlt(_localctx, 1);
				{
				setState(267);
				match(LCB);
				setState(268);
				value();
				setState(269);
				match(RCB);
				}
				break;
			case Q:
				enterOuterAlt(_localctx, 2);
				{
				setState(271);
				match(Q);
				setState(272);
				value();
				setState(273);
				match(Q);
				}
				break;
			case RCB:
			case RB:
			case DASH:
			case AND:
			case BOOK:
			case ARTICLE:
			case CONFERENCE:
			case AUTHOR:
			case TITLE:
			case YEAR:
			case PUBLISHER:
			case JOURNAL:
			case PAGES:
			case VOLUME:
			case NUMBER:
			case BOOKTITLE:
			case MONTH:
			case COMMA:
			case Jan:
			case Feb:
			case Mar:
			case Apr:
			case May:
			case Jun:
			case Jul:
			case Aug:
			case Sep:
			case Oct:
			case Nov:
			case Dec:
			case DIGITS:
			case PUNC:
			case FREETEXT:
				enterOuterAlt(_localctx, 3);
				{
				setState(275);
				value();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ValueContext extends ParserRuleContext {
		public List<FreeContext> free() {
			return getRuleContexts(FreeContext.class);
		}
		public FreeContext free(int i) {
			return getRuleContext(FreeContext.class,i);
		}
		public List<TerminalNode> DIGITS() { return getTokens(Pract3Parser.DIGITS); }
		public TerminalNode DIGITS(int i) {
			return getToken(Pract3Parser.DIGITS, i);
		}
		public ValueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterValue(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitValue(this);
		}
	}

	public final ValueContext value() throws RecognitionException {
		ValueContext _localctx = new ValueContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_value);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(282);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					setState(280);
					switch (_input.LA(1)) {
					case DASH:
					case AND:
					case BOOK:
					case ARTICLE:
					case CONFERENCE:
					case AUTHOR:
					case TITLE:
					case YEAR:
					case PUBLISHER:
					case JOURNAL:
					case PAGES:
					case VOLUME:
					case NUMBER:
					case BOOKTITLE:
					case MONTH:
					case COMMA:
					case Jan:
					case Feb:
					case Mar:
					case Apr:
					case May:
					case Jun:
					case Jul:
					case Aug:
					case Sep:
					case Oct:
					case Nov:
					case Dec:
					case PUNC:
					case FREETEXT:
						{
						setState(278);
						free();
						}
						break;
					case DIGITS:
						{
						setState(279);
						match(DIGITS);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					} 
				}
				setState(284);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,16,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AuthorContext extends ParserRuleContext {
		public TerminalNode LCB() { return getToken(Pract3Parser.LCB, 0); }
		public AuthorlistContext authorlist() {
			return getRuleContext(AuthorlistContext.class,0);
		}
		public TerminalNode RCB() { return getToken(Pract3Parser.RCB, 0); }
		public List<TerminalNode> Q() { return getTokens(Pract3Parser.Q); }
		public TerminalNode Q(int i) {
			return getToken(Pract3Parser.Q, i);
		}
		public AuthorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_author; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterAuthor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitAuthor(this);
		}
	}

	public final AuthorContext author() throws RecognitionException {
		AuthorContext _localctx = new AuthorContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_author);
		try {
			setState(293);
			switch (_input.LA(1)) {
			case LCB:
				enterOuterAlt(_localctx, 1);
				{
				setState(285);
				match(LCB);
				setState(286);
				authorlist();
				setState(287);
				match(RCB);
				}
				break;
			case Q:
				enterOuterAlt(_localctx, 2);
				{
				setState(289);
				match(Q);
				setState(290);
				authorlist();
				setState(291);
				match(Q);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AuthorlistContext extends ParserRuleContext {
		public FreeContext free;
		public List<FreeContext> free() {
			return getRuleContexts(FreeContext.class);
		}
		public FreeContext free(int i) {
			return getRuleContext(FreeContext.class,i);
		}
		public List<TerminalNode> AND() { return getTokens(Pract3Parser.AND); }
		public TerminalNode AND(int i) {
			return getToken(Pract3Parser.AND, i);
		}
		public AuthorlistContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_authorlist; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterAuthorlist(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitAuthorlist(this);
		}
	}

	public final AuthorlistContext authorlist() throws RecognitionException {
		AuthorlistContext _localctx = new AuthorlistContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_authorlist);

		    xmlb(CONTRIBUTORS);
		    xmlb(AUTHORS);

		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(295);
			((AuthorlistContext)_localctx).free = free();
			setState(300);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==AND) {
				{
				{
				setState(296);
				match(AND);
				setState(297);
				((AuthorlistContext)_localctx).free = free();
				}
				}
				setState(302);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}

			    for (String s : (((AuthorlistContext)_localctx).free!=null?_input.getText(((AuthorlistContext)_localctx).free.start,((AuthorlistContext)_localctx).free.stop):null).split("and|AND")) {
			        xmlout(AUTH, s.trim());
			    }

			}

			    xmle(AUTHORS);
			    xmle(CONTRIBUTORS);

		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class StringContext extends ParserRuleContext {
		public List<TerminalNode> Q() { return getTokens(Pract3Parser.Q); }
		public TerminalNode Q(int i) {
			return getToken(Pract3Parser.Q, i);
		}
		public FreeContext free() {
			return getRuleContext(FreeContext.class,0);
		}
		public TerminalNode LCB() { return getToken(Pract3Parser.LCB, 0); }
		public TerminalNode RCB() { return getToken(Pract3Parser.RCB, 0); }
		public StringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitString(this);
		}
	}

	public final StringContext string() throws RecognitionException {
		StringContext _localctx = new StringContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_string);
		try {
			setState(313);
			switch (_input.LA(1)) {
			case Q:
				enterOuterAlt(_localctx, 1);
				{
				setState(305);
				match(Q);
				setState(306);
				free();
				setState(307);
				match(Q);
				}
				break;
			case LCB:
				enterOuterAlt(_localctx, 2);
				{
				setState(309);
				match(LCB);
				setState(310);
				free();
				setState(311);
				match(RCB);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PagesStringContext extends ParserRuleContext {
		public List<TerminalNode> Q() { return getTokens(Pract3Parser.Q); }
		public TerminalNode Q(int i) {
			return getToken(Pract3Parser.Q, i);
		}
		public PagessContext pagess() {
			return getRuleContext(PagessContext.class,0);
		}
		public TerminalNode LCB() { return getToken(Pract3Parser.LCB, 0); }
		public TerminalNode RCB() { return getToken(Pract3Parser.RCB, 0); }
		public PagesStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pagesString; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterPagesString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitPagesString(this);
		}
	}

	public final PagesStringContext pagesString() throws RecognitionException {
		PagesStringContext _localctx = new PagesStringContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_pagesString);
		try {
			setState(323);
			switch (_input.LA(1)) {
			case Q:
				enterOuterAlt(_localctx, 1);
				{
				setState(315);
				match(Q);
				setState(316);
				pagess();
				setState(317);
				match(Q);
				}
				break;
			case LCB:
				enterOuterAlt(_localctx, 2);
				{
				setState(319);
				match(LCB);
				setState(320);
				pagess();
				setState(321);
				match(RCB);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class PagessContext extends ParserRuleContext {
		public List<TerminalNode> DIGITS() { return getTokens(Pract3Parser.DIGITS); }
		public TerminalNode DIGITS(int i) {
			return getToken(Pract3Parser.DIGITS, i);
		}
		public List<TerminalNode> DASH() { return getTokens(Pract3Parser.DASH); }
		public TerminalNode DASH(int i) {
			return getToken(Pract3Parser.DASH, i);
		}
		public PagessContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_pagess; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterPagess(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitPagess(this);
		}
	}

	public final PagessContext pagess() throws RecognitionException {
		PagessContext _localctx = new PagessContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_pagess);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(325);
			match(DIGITS);
			setState(326);
			match(DASH);
			setState(327);
			match(DASH);
			setState(328);
			match(DIGITS);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LabelContext extends ParserRuleContext {
		public TerminalNode FREETEXT() { return getToken(Pract3Parser.FREETEXT, 0); }
		public LabelContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_label; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterLabel(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitLabel(this);
		}
	}

	public final LabelContext label() throws RecognitionException {
		LabelContext _localctx = new LabelContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_label);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(330);
			match(FREETEXT);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SepContext extends ParserRuleContext {
		public TerminalNode COMMA() { return getToken(Pract3Parser.COMMA, 0); }
		public SepContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sep; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterSep(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitSep(this);
		}
	}

	public final SepContext sep() throws RecognitionException {
		SepContext _localctx = new SepContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_sep);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(332);
			match(COMMA);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class QintContext extends ParserRuleContext {
		public TerminalNode DIGITS() { return getToken(Pract3Parser.DIGITS, 0); }
		public List<TerminalNode> Q() { return getTokens(Pract3Parser.Q); }
		public TerminalNode Q(int i) {
			return getToken(Pract3Parser.Q, i);
		}
		public TerminalNode LCB() { return getToken(Pract3Parser.LCB, 0); }
		public TerminalNode RCB() { return getToken(Pract3Parser.RCB, 0); }
		public QintContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_qint; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterQint(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitQint(this);
		}
	}

	public final QintContext qint() throws RecognitionException {
		QintContext _localctx = new QintContext(_ctx, getState());
		enterRule(_localctx, 80, RULE_qint);
		try {
			setState(341);
			switch (_input.LA(1)) {
			case DIGITS:
				enterOuterAlt(_localctx, 1);
				{
				setState(334);
				match(DIGITS);
				}
				break;
			case Q:
				enterOuterAlt(_localctx, 2);
				{
				setState(335);
				match(Q);
				setState(336);
				match(DIGITS);
				setState(337);
				match(Q);
				}
				break;
			case LCB:
				enterOuterAlt(_localctx, 3);
				{
				setState(338);
				match(LCB);
				setState(339);
				match(DIGITS);
				setState(340);
				match(RCB);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MonthStringContext extends ParserRuleContext {
		public List<TerminalNode> Q() { return getTokens(Pract3Parser.Q); }
		public TerminalNode Q(int i) {
			return getToken(Pract3Parser.Q, i);
		}
		public MonthContext month() {
			return getRuleContext(MonthContext.class,0);
		}
		public TerminalNode LCB() { return getToken(Pract3Parser.LCB, 0); }
		public TerminalNode RCB() { return getToken(Pract3Parser.RCB, 0); }
		public MonthStringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_monthString; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterMonthString(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitMonthString(this);
		}
	}

	public final MonthStringContext monthString() throws RecognitionException {
		MonthStringContext _localctx = new MonthStringContext(_ctx, getState());
		enterRule(_localctx, 82, RULE_monthString);
		try {
			setState(351);
			switch (_input.LA(1)) {
			case Q:
				enterOuterAlt(_localctx, 1);
				{
				setState(343);
				match(Q);
				setState(344);
				month();
				setState(345);
				match(Q);
				}
				break;
			case LCB:
				enterOuterAlt(_localctx, 2);
				{
				setState(347);
				match(LCB);
				setState(348);
				month();
				setState(349);
				match(RCB);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MonthContext extends ParserRuleContext {
		public TerminalNode Jan() { return getToken(Pract3Parser.Jan, 0); }
		public TerminalNode Feb() { return getToken(Pract3Parser.Feb, 0); }
		public TerminalNode Mar() { return getToken(Pract3Parser.Mar, 0); }
		public TerminalNode Apr() { return getToken(Pract3Parser.Apr, 0); }
		public TerminalNode May() { return getToken(Pract3Parser.May, 0); }
		public TerminalNode Jun() { return getToken(Pract3Parser.Jun, 0); }
		public TerminalNode Jul() { return getToken(Pract3Parser.Jul, 0); }
		public TerminalNode Aug() { return getToken(Pract3Parser.Aug, 0); }
		public TerminalNode Sep() { return getToken(Pract3Parser.Sep, 0); }
		public TerminalNode Oct() { return getToken(Pract3Parser.Oct, 0); }
		public TerminalNode Nov() { return getToken(Pract3Parser.Nov, 0); }
		public TerminalNode Dec() { return getToken(Pract3Parser.Dec, 0); }
		public TerminalNode DIGITS() { return getToken(Pract3Parser.DIGITS, 0); }
		public MonthContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_month; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterMonth(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitMonth(this);
		}
	}

	public final MonthContext month() throws RecognitionException {
		MonthContext _localctx = new MonthContext(_ctx, getState());
		enterRule(_localctx, 84, RULE_month);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(353);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << Jan) | (1L << Feb) | (1L << Mar) | (1L << Apr) | (1L << May) | (1L << Jun) | (1L << Jul) | (1L << Aug) | (1L << Sep) | (1L << Oct) | (1L << Nov) | (1L << Dec) | (1L << DIGITS))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FreeContext extends ParserRuleContext {
		public List<TerminalNode> FREETEXT() { return getTokens(Pract3Parser.FREETEXT); }
		public TerminalNode FREETEXT(int i) {
			return getToken(Pract3Parser.FREETEXT, i);
		}
		public List<TerminalNode> COMMA() { return getTokens(Pract3Parser.COMMA); }
		public TerminalNode COMMA(int i) {
			return getToken(Pract3Parser.COMMA, i);
		}
		public List<TerminalNode> PUNC() { return getTokens(Pract3Parser.PUNC); }
		public TerminalNode PUNC(int i) {
			return getToken(Pract3Parser.PUNC, i);
		}
		public List<TerminalNode> DASH() { return getTokens(Pract3Parser.DASH); }
		public TerminalNode DASH(int i) {
			return getToken(Pract3Parser.DASH, i);
		}
		public List<KeywordContext> keyword() {
			return getRuleContexts(KeywordContext.class);
		}
		public KeywordContext keyword(int i) {
			return getRuleContext(KeywordContext.class,i);
		}
		public FreeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_free; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterFree(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitFree(this);
		}
	}

	public final FreeContext free() throws RecognitionException {
		FreeContext _localctx = new FreeContext(_ctx, getState());
		enterRule(_localctx, 86, RULE_free);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(360); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					setState(360);
					switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
					case 1:
						{
						setState(355);
						match(FREETEXT);
						}
						break;
					case 2:
						{
						setState(356);
						match(COMMA);
						}
						break;
					case 3:
						{
						setState(357);
						match(PUNC);
						}
						break;
					case 4:
						{
						setState(358);
						match(DASH);
						}
						break;
					case 5:
						{
						setState(359);
						keyword();
						}
						break;
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(362); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class KeywordContext extends ParserRuleContext {
		public TerminalNode AND() { return getToken(Pract3Parser.AND, 0); }
		public TerminalNode BOOK() { return getToken(Pract3Parser.BOOK, 0); }
		public TerminalNode ARTICLE() { return getToken(Pract3Parser.ARTICLE, 0); }
		public TerminalNode CONFERENCE() { return getToken(Pract3Parser.CONFERENCE, 0); }
		public TerminalNode AUTHOR() { return getToken(Pract3Parser.AUTHOR, 0); }
		public TerminalNode TITLE() { return getToken(Pract3Parser.TITLE, 0); }
		public TerminalNode YEAR() { return getToken(Pract3Parser.YEAR, 0); }
		public TerminalNode PUBLISHER() { return getToken(Pract3Parser.PUBLISHER, 0); }
		public TerminalNode JOURNAL() { return getToken(Pract3Parser.JOURNAL, 0); }
		public TerminalNode PAGES() { return getToken(Pract3Parser.PAGES, 0); }
		public TerminalNode VOLUME() { return getToken(Pract3Parser.VOLUME, 0); }
		public TerminalNode NUMBER() { return getToken(Pract3Parser.NUMBER, 0); }
		public TerminalNode BOOKTITLE() { return getToken(Pract3Parser.BOOKTITLE, 0); }
		public TerminalNode MONTH() { return getToken(Pract3Parser.MONTH, 0); }
		public TerminalNode COMMA() { return getToken(Pract3Parser.COMMA, 0); }
		public TerminalNode Jan() { return getToken(Pract3Parser.Jan, 0); }
		public TerminalNode Feb() { return getToken(Pract3Parser.Feb, 0); }
		public TerminalNode Mar() { return getToken(Pract3Parser.Mar, 0); }
		public TerminalNode Apr() { return getToken(Pract3Parser.Apr, 0); }
		public TerminalNode May() { return getToken(Pract3Parser.May, 0); }
		public TerminalNode Jun() { return getToken(Pract3Parser.Jun, 0); }
		public TerminalNode Jul() { return getToken(Pract3Parser.Jul, 0); }
		public TerminalNode Aug() { return getToken(Pract3Parser.Aug, 0); }
		public TerminalNode Sep() { return getToken(Pract3Parser.Sep, 0); }
		public TerminalNode Oct() { return getToken(Pract3Parser.Oct, 0); }
		public TerminalNode Nov() { return getToken(Pract3Parser.Nov, 0); }
		public TerminalNode Dec() { return getToken(Pract3Parser.Dec, 0); }
		public KeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyword; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).enterKeyword(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof Pract3Listener ) ((Pract3Listener)listener).exitKeyword(this);
		}
	}

	public final KeywordContext keyword() throws RecognitionException {
		KeywordContext _localctx = new KeywordContext(_ctx, getState());
		enterRule(_localctx, 88, RULE_keyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(364);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << AND) | (1L << BOOK) | (1L << ARTICLE) | (1L << CONFERENCE) | (1L << AUTHOR) | (1L << TITLE) | (1L << YEAR) | (1L << PUBLISHER) | (1L << JOURNAL) | (1L << PAGES) | (1L << VOLUME) | (1L << NUMBER) | (1L << BOOKTITLE) | (1L << MONTH) | (1L << COMMA) | (1L << Jan) | (1L << Feb) | (1L << Mar) | (1L << Apr) | (1L << May) | (1L << Jun) | (1L << Jul) | (1L << Aug) | (1L << Sep) | (1L << Oct) | (1L << Nov) | (1L << Dec))) != 0)) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2:
			return b_sempred((BContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean b_sempred(BContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3)\u0171\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\4*\t*\4+\t+\4"+
		",\t,\4-\t-\4.\t.\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\7\4e\n\4\f\4\16\4h\13"+
		"\4\3\5\3\5\3\5\5\5m\n\5\3\6\3\6\3\6\5\6r\n\6\3\7\3\7\3\7\3\b\3\b\3\b\3"+
		"\b\3\b\3\b\3\b\3\b\5\b\177\n\b\3\t\3\t\6\t\u0083\n\t\r\t\16\t\u0084\3"+
		"\n\3\n\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u0092\n\13\3\f"+
		"\3\f\3\f\6\f\u0097\n\f\r\f\16\f\u0098\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r"+
		"\3\r\5\r\u00a4\n\r\3\16\3\16\3\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3"+
		"\17\5\17\u00b1\n\17\3\20\3\20\3\20\6\20\u00b6\n\20\r\20\16\20\u00b7\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u00c2\n\21\3\22\3\22\3\22"+
		"\3\23\3\23\3\23\3\23\3\23\3\23\3\23\3\23\5\23\u00cf\n\23\3\24\3\24\3\24"+
		"\6\24\u00d4\n\24\r\24\16\24\u00d5\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u00de"+
		"\n\25\3\26\3\26\3\26\3\26\3\27\3\27\3\27\3\27\3\30\3\30\3\30\3\30\3\31"+
		"\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\32\3\33\3\33\3\33\3\33\3\34\3\34"+
		"\3\34\3\34\3\34\3\35\3\35\3\35\3\35\3\36\3\36\3\36\3\36\3\37\3\37\3\37"+
		"\3\37\3 \3 \3 \3 \3!\3!\3!\3!\3!\3!\3!\3!\3!\5!\u0117\n!\3\"\3\"\7\"\u011b"+
		"\n\"\f\"\16\"\u011e\13\"\3#\3#\3#\3#\3#\3#\3#\3#\5#\u0128\n#\3$\3$\3$"+
		"\7$\u012d\n$\f$\16$\u0130\13$\3$\3$\3%\3%\3%\3%\3%\3%\3%\3%\5%\u013c\n"+
		"%\3&\3&\3&\3&\3&\3&\3&\3&\5&\u0146\n&\3\'\3\'\3\'\3\'\3\'\3(\3(\3)\3)"+
		"\3*\3*\3*\3*\3*\3*\3*\5*\u0158\n*\3+\3+\3+\3+\3+\3+\3+\3+\5+\u0162\n+"+
		"\3,\3,\3-\3-\3-\3-\3-\6-\u016b\n-\r-\16-\u016c\3.\3.\3.\2\3\6/\2\4\6\b"+
		"\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNPRTVX"+
		"Z\2\4\3\2\33\'\3\2\f&\u0170\2\\\3\2\2\2\4^\3\2\2\2\6a\3\2\2\2\bi\3\2\2"+
		"\2\nq\3\2\2\2\fs\3\2\2\2\16~\3\2\2\2\20\u0080\3\2\2\2\22\u0086\3\2\2\2"+
		"\24\u0091\3\2\2\2\26\u0093\3\2\2\2\30\u009a\3\2\2\2\32\u00a5\3\2\2\2\34"+
		"\u00b0\3\2\2\2\36\u00b2\3\2\2\2 \u00b9\3\2\2\2\"\u00c3\3\2\2\2$\u00ce"+
		"\3\2\2\2&\u00d0\3\2\2\2(\u00d7\3\2\2\2*\u00df\3\2\2\2,\u00e3\3\2\2\2."+
		"\u00e7\3\2\2\2\60\u00eb\3\2\2\2\62\u00f0\3\2\2\2\64\u00f4\3\2\2\2\66\u00f8"+
		"\3\2\2\28\u00fd\3\2\2\2:\u0101\3\2\2\2<\u0105\3\2\2\2>\u0109\3\2\2\2@"+
		"\u0116\3\2\2\2B\u011c\3\2\2\2D\u0127\3\2\2\2F\u0129\3\2\2\2H\u013b\3\2"+
		"\2\2J\u0145\3\2\2\2L\u0147\3\2\2\2N\u014c\3\2\2\2P\u014e\3\2\2\2R\u0157"+
		"\3\2\2\2T\u0161\3\2\2\2V\u0163\3\2\2\2X\u016a\3\2\2\2Z\u016e\3\2\2\2\\"+
		"]\5\4\3\2]\3\3\2\2\2^_\5\6\4\2_`\7\2\2\3`\5\3\2\2\2af\b\4\1\2bc\f\4\2"+
		"\2ce\5\b\5\2db\3\2\2\2eh\3\2\2\2fd\3\2\2\2fg\3\2\2\2g\7\3\2\2\2hf\3\2"+
		"\2\2il\7\4\2\2jm\5\n\6\2km\5\f\7\2lj\3\2\2\2lk\3\2\2\2m\t\3\2\2\2nr\5"+
		"\"\22\2or\5\22\n\2pr\5\32\16\2qn\3\2\2\2qo\3\2\2\2qp\3\2\2\2r\13\3\2\2"+
		"\2st\5N(\2tu\5\16\b\2u\r\3\2\2\2vw\7\5\2\2wx\5\20\t\2xy\7\6\2\2y\177\3"+
		"\2\2\2z{\7\7\2\2{|\5\20\t\2|}\7\b\2\2}\177\3\2\2\2~v\3\2\2\2~z\3\2\2\2"+
		"\177\17\3\2\2\2\u0080\u0082\5N(\2\u0081\u0083\5> \2\u0082\u0081\3\2\2"+
		"\2\u0083\u0084\3\2\2\2\u0084\u0082\3\2\2\2\u0084\u0085\3\2\2\2\u0085\21"+
		"\3\2\2\2\u0086\u0087\7\16\2\2\u0087\u0088\5\24\13\2\u0088\23\3\2\2\2\u0089"+
		"\u008a\7\5\2\2\u008a\u008b\5\26\f\2\u008b\u008c\7\6\2\2\u008c\u0092\3"+
		"\2\2\2\u008d\u008e\7\7\2\2\u008e\u008f\5\26\f\2\u008f\u0090\7\b\2\2\u0090"+
		"\u0092\3\2\2\2\u0091\u0089\3\2\2\2\u0091\u008d\3\2\2\2\u0092\25\3\2\2"+
		"\2\u0093\u0094\5N(\2\u0094\u0096\b\f\1\2\u0095\u0097\5\30\r\2\u0096\u0095"+
		"\3\2\2\2\u0097\u0098\3\2\2\2\u0098\u0096\3\2\2\2\u0098\u0099\3\2\2\2\u0099"+
		"\27\3\2\2\2\u009a\u00a3\5P)\2\u009b\u00a4\5*\26\2\u009c\u00a4\5\60\31"+
		"\2\u009d\u00a4\5\64\33\2\u009e\u00a4\5\66\34\2\u009f\u00a4\5:\36\2\u00a0"+
		"\u00a4\5<\37\2\u00a1\u00a4\5,\27\2\u00a2\u00a4\5> \2\u00a3\u009b\3\2\2"+
		"\2\u00a3\u009c\3\2\2\2\u00a3\u009d\3\2\2\2\u00a3\u009e\3\2\2\2\u00a3\u009f"+
		"\3\2\2\2\u00a3\u00a0\3\2\2\2\u00a3\u00a1\3\2\2\2\u00a3\u00a2\3\2\2\2\u00a4"+
		"\31\3\2\2\2\u00a5\u00a6\7\17\2\2\u00a6\u00a7\5\34\17\2\u00a7\33\3\2\2"+
		"\2\u00a8\u00a9\7\5\2\2\u00a9\u00aa\5\36\20\2\u00aa\u00ab\7\6\2\2\u00ab"+
		"\u00b1\3\2\2\2\u00ac\u00ad\7\7\2\2\u00ad\u00ae\5\36\20\2\u00ae\u00af\7"+
		"\b\2\2\u00af\u00b1\3\2\2\2\u00b0\u00a8\3\2\2\2\u00b0\u00ac\3\2\2\2\u00b1"+
		"\35\3\2\2\2\u00b2\u00b3\5N(\2\u00b3\u00b5\b\20\1\2\u00b4\u00b6\5 \21\2"+
		"\u00b5\u00b4\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00b5\3\2\2\2\u00b7\u00b8"+
		"\3\2\2\2\u00b8\37\3\2\2\2\u00b9\u00c1\5P)\2\u00ba\u00c2\5*\26\2\u00bb"+
		"\u00c2\5\60\31\2\u00bc\u00c2\58\35\2\u00bd\u00c2\5,\27\2\u00be\u00c2\5"+
		".\30\2\u00bf\u00c2\5\66\34\2\u00c0\u00c2\5> \2\u00c1\u00ba\3\2\2\2\u00c1"+
		"\u00bb\3\2\2\2\u00c1\u00bc\3\2\2\2\u00c1\u00bd\3\2\2\2\u00c1\u00be\3\2"+
		"\2\2\u00c1\u00bf\3\2\2\2\u00c1\u00c0\3\2\2\2\u00c2!\3\2\2\2\u00c3\u00c4"+
		"\7\r\2\2\u00c4\u00c5\5$\23\2\u00c5#\3\2\2\2\u00c6\u00c7\7\5\2\2\u00c7"+
		"\u00c8\5&\24\2\u00c8\u00c9\7\6\2\2\u00c9\u00cf\3\2\2\2\u00ca\u00cb\7\7"+
		"\2\2\u00cb\u00cc\5&\24\2\u00cc\u00cd\7\b\2\2\u00cd\u00cf\3\2\2\2\u00ce"+
		"\u00c6\3\2\2\2\u00ce\u00ca\3\2\2\2\u00cf%\3\2\2\2\u00d0\u00d1\5N(\2\u00d1"+
		"\u00d3\b\24\1\2\u00d2\u00d4\5(\25\2\u00d3\u00d2\3\2\2\2\u00d4\u00d5\3"+
		"\2\2\2\u00d5\u00d3\3\2\2\2\u00d5\u00d6\3\2\2\2\u00d6\'\3\2\2\2\u00d7\u00dd"+
		"\5P)\2\u00d8\u00de\5*\26\2\u00d9\u00de\5\60\31\2\u00da\u00de\5,\27\2\u00db"+
		"\u00de\5\62\32\2\u00dc\u00de\5> \2\u00dd\u00d8\3\2\2\2\u00dd\u00d9\3\2"+
		"\2\2\u00dd\u00da\3\2\2\2\u00dd\u00db\3\2\2\2\u00dd\u00dc\3\2\2\2\u00de"+
		")\3\2\2\2\u00df\u00e0\7\20\2\2\u00e0\u00e1\7\n\2\2\u00e1\u00e2\5D#\2\u00e2"+
		"+\3\2\2\2\u00e3\u00e4\7\22\2\2\u00e4\u00e5\7\n\2\2\u00e5\u00e6\5R*\2\u00e6"+
		"-\3\2\2\2\u00e7\u00e8\7\31\2\2\u00e8\u00e9\7\n\2\2\u00e9\u00ea\5T+\2\u00ea"+
		"/\3\2\2\2\u00eb\u00ec\7\21\2\2\u00ec\u00ed\7\n\2\2\u00ed\u00ee\5H%\2\u00ee"+
		"\u00ef\b\31\1\2\u00ef\61\3\2\2\2\u00f0\u00f1\7\23\2\2\u00f1\u00f2\7\n"+
		"\2\2\u00f2\u00f3\5H%\2\u00f3\63\3\2\2\2\u00f4\u00f5\7\24\2\2\u00f5\u00f6"+
		"\7\n\2\2\u00f6\u00f7\5H%\2\u00f7\65\3\2\2\2\u00f8\u00f9\7\25\2\2\u00f9"+
		"\u00fa\7\n\2\2\u00fa\u00fb\5J&\2\u00fb\u00fc\b\34\1\2\u00fc\67\3\2\2\2"+
		"\u00fd\u00fe\7\30\2\2\u00fe\u00ff\7\n\2\2\u00ff\u0100\5H%\2\u01009\3\2"+
		"\2\2\u0101\u0102\7\26\2\2\u0102\u0103\7\n\2\2\u0103\u0104\5R*\2\u0104"+
		";\3\2\2\2\u0105\u0106\7\27\2\2\u0106\u0107\7\n\2\2\u0107\u0108\5R*\2\u0108"+
		"=\3\2\2\2\u0109\u010a\5X-\2\u010a\u010b\7\n\2\2\u010b\u010c\5@!\2\u010c"+
		"?\3\2\2\2\u010d\u010e\7\5\2\2\u010e\u010f\5B\"\2\u010f\u0110\7\6\2\2\u0110"+
		"\u0117\3\2\2\2\u0111\u0112\7\t\2\2\u0112\u0113\5B\"\2\u0113\u0114\7\t"+
		"\2\2\u0114\u0117\3\2\2\2\u0115\u0117\5B\"\2\u0116\u010d\3\2\2\2\u0116"+
		"\u0111\3\2\2\2\u0116\u0115\3\2\2\2\u0117A\3\2\2\2\u0118\u011b\5X-\2\u0119"+
		"\u011b\7\'\2\2\u011a\u0118\3\2\2\2\u011a\u0119\3\2\2\2\u011b\u011e\3\2"+
		"\2\2\u011c\u011a\3\2\2\2\u011c\u011d\3\2\2\2\u011dC\3\2\2\2\u011e\u011c"+
		"\3\2\2\2\u011f\u0120\7\5\2\2\u0120\u0121\5F$\2\u0121\u0122\7\6\2\2\u0122"+
		"\u0128\3\2\2\2\u0123\u0124\7\t\2\2\u0124\u0125\5F$\2\u0125\u0126\7\t\2"+
		"\2\u0126\u0128\3\2\2\2\u0127\u011f\3\2\2\2\u0127\u0123\3\2\2\2\u0128E"+
		"\3\2\2\2\u0129\u012e\5X-\2\u012a\u012b\7\f\2\2\u012b\u012d\5X-\2\u012c"+
		"\u012a\3\2\2\2\u012d\u0130\3\2\2\2\u012e\u012c\3\2\2\2\u012e\u012f\3\2"+
		"\2\2\u012f\u0131\3\2\2\2\u0130\u012e\3\2\2\2\u0131\u0132\b$\1\2\u0132"+
		"G\3\2\2\2\u0133\u0134\7\t\2\2\u0134\u0135\5X-\2\u0135\u0136\7\t\2\2\u0136"+
		"\u013c\3\2\2\2\u0137\u0138\7\5\2\2\u0138\u0139\5X-\2\u0139\u013a\7\6\2"+
		"\2\u013a\u013c\3\2\2\2\u013b\u0133\3\2\2\2\u013b\u0137\3\2\2\2\u013cI"+
		"\3\2\2\2\u013d\u013e\7\t\2\2\u013e\u013f\5L\'\2\u013f\u0140\7\t\2\2\u0140"+
		"\u0146\3\2\2\2\u0141\u0142\7\5\2\2\u0142\u0143\5L\'\2\u0143\u0144\7\6"+
		"\2\2\u0144\u0146\3\2\2\2\u0145\u013d\3\2\2\2\u0145\u0141\3\2\2\2\u0146"+
		"K\3\2\2\2\u0147\u0148\7\'\2\2\u0148\u0149\7\13\2\2\u0149\u014a\7\13\2"+
		"\2\u014a\u014b\7\'\2\2\u014bM\3\2\2\2\u014c\u014d\7)\2\2\u014dO\3\2\2"+
		"\2\u014e\u014f\7\32\2\2\u014fQ\3\2\2\2\u0150\u0158\7\'\2\2\u0151\u0152"+
		"\7\t\2\2\u0152\u0153\7\'\2\2\u0153\u0158\7\t\2\2\u0154\u0155\7\5\2\2\u0155"+
		"\u0156\7\'\2\2\u0156\u0158\7\6\2\2\u0157\u0150\3\2\2\2\u0157\u0151\3\2"+
		"\2\2\u0157\u0154\3\2\2\2\u0158S\3\2\2\2\u0159\u015a\7\t\2\2\u015a\u015b"+
		"\5V,\2\u015b\u015c\7\t\2\2\u015c\u0162\3\2\2\2\u015d\u015e\7\5\2\2\u015e"+
		"\u015f\5V,\2\u015f\u0160\7\6\2\2\u0160\u0162\3\2\2\2\u0161\u0159\3\2\2"+
		"\2\u0161\u015d\3\2\2\2\u0162U\3\2\2\2\u0163\u0164\t\2\2\2\u0164W\3\2\2"+
		"\2\u0165\u016b\7)\2\2\u0166\u016b\7\32\2\2\u0167\u016b\7(\2\2\u0168\u016b"+
		"\7\13\2\2\u0169\u016b\5Z.\2\u016a\u0165\3\2\2\2\u016a\u0166\3\2\2\2\u016a"+
		"\u0167\3\2\2\2\u016a\u0168\3\2\2\2\u016a\u0169\3\2\2\2\u016b\u016c\3\2"+
		"\2\2\u016c\u016a\3\2\2\2\u016c\u016d\3\2\2\2\u016dY\3\2\2\2\u016e\u016f"+
		"\t\3\2\2\u016f[\3\2\2\2\33flq~\u0084\u0091\u0098\u00a3\u00b0\u00b7\u00c1"+
		"\u00ce\u00d5\u00dd\u0116\u011a\u011c\u0127\u012e\u013b\u0145\u0157\u0161"+
		"\u016a\u016c";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}