---
layout: post
title: Tools and Technologies
categories:
- Research and Design
tags:
- eclipse
- rcp
- gef
- jai
- xml
- vtd
- java
---

I just realized that I forgot to mention the programming language and technologies I'm going to use in this project so here's a quick update just about that.

This tool will be written in Java on top of [Eclipse Rich Client Platform (RCP)][rcp]. Eclipse RCP is an excellent framework for fast application development. I'm quite familiar with it since I'm actually working on Eclipse-based tools at work.

For sprite sheet editing I'm going to use [Eclipse Graphical Editing Framework (GEF)][gef] which provides many features for creating  editors for visual editing of arbitrary models. This framework will only be used for rendering and editing sprite sheets. The loading of various image formats will be implemented with Java Advanced Imaging API, but of course at the beginning, to keep things simple,  I will use Java's native ImageReader. Might even stick with it if I get lazy! ;)

For XML parsing I'm going to use [VTD-XML][vtd_xml]. I was introduced to this parser at work. I've never used it but it is claimed to be the best XML parser there is! Check out its benchmark pages, stats are pretty impressive.

[rcp]: http://wiki.eclipse.org/index.php/Rich_Client_Platform "Eclipse Rich Client Platform"
[gef]: http://www.eclipse.org/gef "Eclipse Graphical Editing Framework"
[vtd_xml]: http://vtd-xml.sourceforge.net "VTD-XML"
