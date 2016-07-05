#!/usr/bin/env python
# encoding: utf-8
'''
Created on Jun 4, 2016

@author:     Suresh Hewapathirana
'''

import xml.sax
from MzIdentMLHandler import MzIdentMLHandler

class SequenceExtractor(xml.sax.ContentHandler):

    '''
    This is a class which initiates the file reading and
    return the database sequence to the caller.
    '''
    def __init__(self):
        pass;

    def extract(self,filepath,dbsequenceid):

        # create an XMLReader
        parser = xml.sax.make_parser()

        # turn off namespaces
        parser.setFeature(xml.sax.handler.feature_namespaces, 0)

        # variable to hold the sequence
        sequenceString = "Not Available"

        try:
            # override the default ContextHandler
            Handler = MzIdentMLHandler(dbsequenceid)
            parser.setContentHandler(Handler)
            parser.parse(filepath)
        except xml.sax.SAXException as err:
            # get the sequence which has passed with argument[0]
            sequenceString = err.args[0]
            return sequenceString