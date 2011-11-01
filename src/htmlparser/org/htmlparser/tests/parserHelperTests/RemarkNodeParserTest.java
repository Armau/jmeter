/*
 * ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in
 * the documentation and/or other materials provided with the
 * distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 * if any, must include the following acknowledgment:
 * "This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/)."
 * Alternately, this acknowledgment may appear in the software itself,
 * if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 * "Apache JMeter" must not be used to endorse or promote products
 * derived from this software without prior written permission. For
 * written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 * "Apache JMeter", nor may "Apache" appear in their name, without
 * prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 * 
 */

// The developers of JMeter and Apache are greatful to the developers
// of HTMLParser for giving Apache Software Foundation a non-exclusive
// license. The performance benefits of HTMLParser are clear and the
// users of JMeter will benefit from the hard work the HTMLParser
// team. For detailed information about HTMLParser, the project is
// hosted on sourceforge at http://htmlparser.sourceforge.net/.
//
// HTMLParser was originally created by Somik Raha in 2000. Since then
// a healthy community of users has formed and helped refine the
// design so that it is able to tackle the difficult task of parsing
// dirty HTML. Derrick Oswald is the current lead developer and was kind
// enough to assist JMeter.

package org.htmlparser.tests.parserHelperTests;

import org.htmlparser.Parser;
import org.htmlparser.RemarkNode;
import org.htmlparser.StringNode;
import org.htmlparser.tags.Tag;
import org.htmlparser.tests.ParserTestCase;
import org.htmlparser.util.ParserException;

public class RemarkNodeParserTest extends ParserTestCase
{
    public RemarkNodeParserTest(String name)
    {
        super(name);
    }

    /**
     * The bug being reproduced is this : <BR>
     * &lt;!-- saved from url=(0022)http://internet.e-mail --&gt;
     * &lt;HTML&gt;
     * &lt;HEAD&gt;&lt;META name="title" content="Training Introduction"&gt;
     * &lt;META name="subject" content=""&gt;
     * &lt;!--
         Whats gonna happen now ?
     * --&gt;
     * &lt;TEST&gt;
     * &lt;/TEST&gt;
     * 
     * The above line is incorrectly parsed - the remark is not correctly identified.
     * This bug was reported by Serge Kruppa (2002-Feb-08). 
     */
    public void testRemarkNodeBug() throws ParserException
    {
        createParser(
            "<!-- saved from url=(0022)http://internet.e-mail -->\n"
                + "<HTML>\n"
                + "<HEAD><META name=\"title\" content=\"Training Introduction\">\n"
                + "<META name=\"subject\" content=\"\">\n"
                + "<!--\n"
                + "   Whats gonna happen now ?\n"
                + "-->\n"
                + "<TEST>\n"
                + "</TEST>\n");
        Parser.setLineSeparator("\r\n");
        parseAndAssertNodeCount(8);
        // The first node should be a HTMLRemarkNode
        assertTrue(
            "First node should be a HTMLRemarkNode",
            node[0] instanceof RemarkNode);
        RemarkNode remarkNode = (RemarkNode) node[0];
        assertEquals(
            "Text of the remarkNode #1",
            " saved from url=(0022)http://internet.e-mail ",
            remarkNode.getText());
        // The sixth node should be a HTMLRemarkNode 
        assertTrue(
            "Sixth node should be a HTMLRemarkNode",
            node[5] instanceof RemarkNode);
        remarkNode = (RemarkNode) node[5];
        assertEquals(
            "Text of the remarkNode #6",
            "\r\n   Whats gonna happen now ?\r\n",
            remarkNode.getText());
    }

    public void testToPlainTextString() throws ParserException
    {
        createParser(
            "<!-- saved from url=(0022)http://internet.e-mail -->\n"
                + "<HTML>\n"
                + "<HEAD><META name=\"title\" content=\"Training Introduction\">\n"
                + "<META name=\"subject\" content=\"\">\n"
                + "<!--\n"
                + "   Whats gonna happen now ?\n"
                + "-->\n"
                + "<TEST>\n"
                + "</TEST>\n");
        Parser.setLineSeparator("\r\n");
        parseAndAssertNodeCount(8);
        // The first node should be a HTMLRemarkNode
        assertTrue(
            "First node should be a HTMLRemarkNode",
            node[0] instanceof RemarkNode);
        RemarkNode remarkNode = (RemarkNode) node[0];
        assertEquals(
            "Plain Text of the remarkNode #1",
            " saved from url=(0022)http://internet.e-mail ",
            remarkNode.toPlainTextString());
        // The sixth node should be a HTMLRemarkNode 
        assertTrue(
            "Sixth node should be a HTMLRemarkNode",
            node[5] instanceof RemarkNode);
        remarkNode = (RemarkNode) node[5];
        assertEquals(
            "Plain Text of the remarkNode #6",
            "\r\n   Whats gonna happen now ?\r\n",
            remarkNode.getText());

    }

    public void testToRawString() throws ParserException
    {
        createParser(
            "<!-- saved from url=(0022)http://internet.e-mail -->\n"
                + "<HTML>\n"
                + "<HEAD><META name=\"title\" content=\"Training Introduction\">\n"
                + "<META name=\"subject\" content=\"\">\n"
                + "<!--\n"
                + "   Whats gonna happen now ?\n"
                + "-->\n"
                + "<TEST>\n"
                + "</TEST>\n");
        Parser.setLineSeparator("\r\n");
        parseAndAssertNodeCount(8);
        // The first node should be a HTMLRemarkNode
        assertTrue(
            "First node should be a HTMLRemarkNode",
            node[0] instanceof RemarkNode);
        RemarkNode remarkNode = (RemarkNode) node[0];
        assertStringEquals(
            "Raw String of the remarkNode #1",
            "<!-- saved from url=(0022)http://internet.e-mail -->",
            remarkNode.toHtml());
        // The sixth node should be a HTMLRemarkNode 
        assertTrue(
            "Sixth node should be a HTMLRemarkNode",
            node[5] instanceof RemarkNode);
        remarkNode = (RemarkNode) node[5];
        assertStringEquals(
            "Raw String of the remarkNode #6",
            "<!--\r\n   Whats gonna happen now ?\r\n-->",
            remarkNode.toHtml());
    }

    public void testNonRemarkNode() throws ParserException
    {
        createParser("&nbsp;<![endif]>");
        parseAndAssertNodeCount(2);
        // The first node should be a HTMLRemarkNode
        assertTrue(
            "First node should be a string node",
            node[0] instanceof StringNode);
        assertTrue("Second node should be a Tag", node[1] instanceof Tag);
        StringNode stringNode = (StringNode) node[0];
        Tag tag = (Tag) node[1];
        assertEquals("Text contents", "&nbsp;", stringNode.getText());
        assertEquals("Tag Contents", "![endif]", tag.getText());

    }

    /**
     * This is the simulation of bug report 586756, submitted
     * by John Zook.
     * If all the comment contains is a blank line, it breaks
     * the state
     */
    public void testRemarkNodeWithBlankLine() throws ParserException
    {
        createParser("<!--\n" + "\n" + "-->");
        Parser.setLineSeparator("\r\n");
        parseAndAssertNodeCount(1);
        assertTrue(
            "Node should be a HTMLRemarkNode",
            node[0] instanceof RemarkNode);
        RemarkNode remarkNode = (RemarkNode) node[0];
        assertEquals("Expected contents", "\r\n", remarkNode.getText());

    }

    /**
     * This is the simulation of a bug report submitted
     * by Claude Duguay.
     * If it is a comment with nothing in it, parser crashes
     */
    public void testRemarkNodeWithNothing() throws ParserException
    {
        createParser("<!-->");
        parseAndAssertNodeCount(1);
        assertTrue(
            "Node should be a HTMLRemarkNode",
            node[0] instanceof RemarkNode);
        RemarkNode remarkNode = (RemarkNode) node[0];
        assertEquals("Expected contents", "", remarkNode.getText());

    }

    /**
     * Reproduction of bug reported by John Zook [594301]
     * When we have tags like :
     * &lt;!-- &lt;A&gt; --&gt;
     * it doesent get parsed correctly
     */
    public void testTagWithinRemarkNode() throws ParserException
    {
        createParser("<!-- \n" + "<A>\n" + "bcd -->");
        Parser.setLineSeparator("\n");
        parseAndAssertNodeCount(1);
        assertTrue(
            "Node should be a HTMLRemarkNode",
            node[0] instanceof RemarkNode);
        RemarkNode remarkNode = (RemarkNode) node[0];
        assertStringEquals(
            "Expected contents",
            " \n<A>\nbcd ",
            remarkNode.getText());

    }

    /**
     * Bug reported by John Zook [594301], invalid remark nodes are accepted as remark nodes.
     * &lt;<br>
     * -<br>
     * -<br>
     * ssd --&gt;<br>
     * This is not supposed to be a remarknode
     */
    public void testInvalidTag() throws ParserException
    {
        createParser("<!\n" + "-\n" + "-\n" + "ssd -->");
        Parser.setLineSeparator("\n");
        parseAndAssertNodeCount(1);
        assertTrue(
            "Node should be a Tag but was " + node[0],
            node[0] instanceof Tag);
        Tag tag = (Tag) node[0];
        assertStringEquals(
            "Expected contents",
            "!\n" + "-\n" + "-\n" + "ssd --",
            tag.getText());
        Parser.setLineSeparator("\r\n");
    }

    /**
     * Bug reported by John Zook [594301]
     * If dashes exist in a comment, they dont get added to the comment text
     */
    public void testDashesInComment() throws ParserException
    {
        createParser("<!-- -- -->");
        parseAndAssertNodeCount(1);
        assertTrue(
            "Node should be a HTMLRemarkNode but was " + node[0],
            node[0] instanceof RemarkNode);
        RemarkNode remarkNode = (RemarkNode) node[0];
        assertEquals("Remark Node contents", " -- ", remarkNode.getText());
    }

    // from http://www.w3.org/MarkUp/html-spec/html-spec_3.html
    //Comments
    //
    //To include comments in an HTML document, use a comment declaration.
    //A comment declaration consists of `<!' followed by zero or more comments
    //followed by `>'. Each comment starts with `--' and includes all text up to
    //and including the next occurrence of `--'. In a comment declaration, white
    //space is allowed after each comment, but not before the first comment. The
    //entire comment declaration is ignored. (10)
    //
    //For example:
    //
    //<!DOCTYPE HTML PUBLIC "-//IETF//DTD HTML 2.0//EN">
    //<HEAD>
    //<TITLE>HTML Comment Example</TITLE>
    //<!-- Id: html-sgml.sgm,v 1.5 1995/05/26 21:29:50 connolly Exp  -->
    //<!-- another -- -- comment -->
    //<!>
    //</HEAD>
    //<BODY>
    //<p> <!- not a comment, just regular old data characters ->

    /**
     * Test a comment declaration with a comment.
     */
    public void testSingleComment() throws ParserException
    {
        createParser(
            "<HTML>\n"
                + "<HEAD>\n"
                + "<TITLE>HTML Comment Test</TITLE>\n"
                + "</HEAD>\n"
                + "<BODY>\n"
                + "<!-- Id: html-sgml.sgm,v 1.5 1995/05/26 21:29:50 connolly Exp  -->\n"
                + "</BODY>\n"
                + "</HTML>\n");
        parseAndAssertNodeCount(10);
        assertTrue(
            "Node should be a HTMLRemarkNode but was " + node[7],
            node[7] instanceof RemarkNode);
        RemarkNode remarkNode = (RemarkNode) node[7];
        assertEquals(
            "Remark Node contents",
            " Id: html-sgml.sgm,v 1.5 1995/05/26 21:29:50 connolly Exp  ",
            remarkNode.getText());
    }

    /**
     * Test a comment declaration with two comments.
     */
    public void testDoubleComment() throws ParserException
    {
        createParser(
            "<HTML>\n"
                + "<HEAD>\n"
                + "<TITLE>HTML Comment Test</TITLE>\n"
                + "</HEAD>\n"
                + "<BODY>\n"
                + "<!-- another -- -- comment -->\n"
                + "</BODY>\n"
                + "</HTML>\n");
        parseAndAssertNodeCount(10);
        assertTrue(
            "Node should be a HTMLRemarkNode but was " + node[7],
            node[7] instanceof RemarkNode);
        RemarkNode remarkNode = (RemarkNode) node[7];
        assertEquals(
            "Remark Node contents",
            " another -- -- comment ",
            remarkNode.getText());
    }

    /**
     * Test a comment declaration without any comments.
     */
    public void testEmptyComment() throws ParserException
    {
        createParser(
            "<HTML>\n"
                + "<HEAD>\n"
                + "<TITLE>HTML Comment Test 'testEmptyComment'</TITLE>\n"
                + "</HEAD>\n"
                + "<BODY>\n"
                + "<!>\n"
                + "</BODY>\n"
                + "</HTML>\n");
        parseAndAssertNodeCount(10);
        assertTrue(
            "Node should be a HTMLRemarkNode but was " + node[7],
            node[7] instanceof RemarkNode);
        RemarkNode remarkNode = (RemarkNode) node[7];
        assertEquals("Remark Node contents", "", remarkNode.getText());
    }

    //    /**
    //     * Test what the specification calls data characters.
    //     * Actually, no browser I've tried handles this correctly (as text).
    //     * Some handle it as a comment and others handle it as a tag.
    //     * So for now we leave this test case out.
    //     */
    //    public void testNotAComment ()
    //        throws
    //            HTMLParserException
    //    {
    //		createParser(
    //              "<HTML>\n"
    //            + "<HEAD>\n"
    //            + "<TITLE>HTML Comment Test 'testNotAComment'</TITLE>\n"
    //            + "</HEAD>\n"
    //            + "<BODY>\n"
    //            + "<!- not a comment, just regular old data characters ->\n"
    //            + "</BODY>\n"
    //            + "</HTML>\n"
    //            );
    //		parseAndAssertNodeCount(10);
    //		assertTrue("Node should not be a HTMLRemarkNode",!(node[7] instanceof HTMLRemarkNode));
    //		assertTrue("Node should be a HTMLStringNode but was "+node[7],node[7].getType()==HTMLStringNode.TYPE);
    //		HTMLStringNode stringNode = (HTMLStringNode)node[7];
    //		assertEquals("String Node contents","<!- not a comment, just regular old data characters ->\n",stringNode.getText());
    //    }
}