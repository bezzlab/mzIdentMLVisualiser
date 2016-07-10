#!/usr/bin/env python
# encoding: utf-8

'''
Created on Jun 4, 2016

@author:     Suresh Hewapathirana
'''
import xml.sax

class MzIdentMLHandler(xml.sax.ContentHandler):
    """
    This is a handler class which override the functionality of default SAX handler.
    This provides overridden methods of SAX handler interface. This contains main
    three methods: startElement,endElement and characters. While streaming the file,
    once we found the sequence, we immediately terminate the execution by forcibly
    raising an exception along with the sequence.
    """

    def __init__(self, dbSeqenceId):
        self.CurrentData = ""
        self.seq = "Not Available"
        self.isFound = False;
        self.dbSeqenceId = dbSeqenceId;

    # Call when an element starts
    def startElement(self, tag, attributes):
        self.CurrentData = tag

        # if there is no <seq> tag found, or passed the DBSequence section
        # we immediately terminate file reading
        if self.isFound == True and (tag == "DBSequence" or tag == "AnalysisCollection"):
            raise xml.sax.SAXException("Not Available")

        if tag == "DBSequence":
            dbSeqid = attributes["id"]
            # if matching Sequence record found, mark the flag as found
            if(dbSeqid == self.dbSeqenceId):
                self.isFound = True

    # Call when an elements ends
    def endElement(self, tag):
        # if there is no seq tag found, need to terminate as quick as possible
        if self.CurrentData == "Seq" and self.isFound == True:
            raise xml.sax.SAXException(self.seq)

    # Call when a character is read
    def characters(self, content):
        if self.CurrentData == "Seq" and self.isFound == True:
            self.seq = content

