---
layout: post
title: Add Sprites - Changed
categories:
- Implementation
tags:
- sprite
- packer
- spritepacker
- sheet
- alan
- morey
- spritey
- batch
- rename
- import
- export
---
<p>As I was working on my <a href="https://www.pivotaltracker.com/story/show/4144408" target="_blank">next user story</a> I stumbled upon a problem (more like my misjudgement than a problem) that was unsolvable without changing the current implementation of importing sprites too much.</p>
<p>Initially, I thought  that when sprite sheet configuration is exported as XML, group and sprite names should be exported as tags to allow users access nodes directly with <a href="http://en.wikipedia.org/wiki/XPath" target="_blank">XPath</a>. So, for example, the following sprite tree</p>
<p><a href="http://bykovskyy.com/spritey/blog/wp-content/uploads/2010/11/sprite-tree-example.png"><img class="aligncenter size-full wp-image-494" title="sprite-tree-example" src="http://bykovskyy.com/spritey/blog/wp-content/uploads/2010/11/sprite-tree-example.png" alt="" width="121" height="72" /></a></p>
<p>would've produced the following configuration</p>
<pre lang="xml">
<sheet>
    <buttons>
        <default x="" y="" width="" height="" />
        <pressed x="" y="" width="" height="" />
    </buttons>
</sheet>
</pre>
<p>But, I did not take into account that I had no character restrictions on names and that they could include characters deemed illegal by <a href="http://www.w3.org/TR/REC-xml/" target="_blank">W3C XML specification</a>.</p>
<p>To fix this I decided to allow only characters defined by XML specification, fix names that contained illegal characters and let users change names manually when importing sprites into sprite sheet, if they wish so. I also decided to make the importing process a little bit more generic, as before, adding a single sprite and adding a whole folder of sprites were two separate operations.</p>
<p>I combined the two separate operations into one single wizard. Users can now specify whether they want to add a couple of sprites or the whole folder of sprites in one wizard. Sprites will then be loaded and displayed in a preview tree where users can choose which sprites to add to a sprite sheet. The image bellow illustrates this process.</p>
<p><a href="http://bykovskyy.com/spritey/blog/wp-content/uploads/2010/11/add-sprites-import-page.png"><img class="aligncenter size-medium wp-image-518" title="add-sprites-import-page" src="http://bykovskyy.com/spritey/blog/wp-content/uploads/2010/11/add-sprites-import-page-300x224.png" alt="" width="300" height="224" /></a></p>
<p>When users are happy with selected sprites they can move to the next page where they can rename sprites and fix conflicts. At the moment, users can only rename one sprite at a time but in the future users will be able to rename a batch of sprites all at once. Controls will look something similar to Find/Replace tool in Eclipse.</p>
<p><a href="http://bykovskyy.com/spritey/blog/wp-content/uploads/2010/11/add-sprites-rename-page.png"><img class="aligncenter size-medium wp-image-520" title="add-sprites-rename-page" src="http://bykovskyy.com/spritey/blog/wp-content/uploads/2010/11/add-sprites-rename-page-300x224.png" alt="" width="300" height="224" /></a></p>
<p>As I was close to finishing these changes I talked to <a href="http://alanmorey.com/" target="_blank">Alan Morey</a> to get his input about this. He suggested that exporting names as tags was not a good idea and that instead I should export names just like other attributes to have something like this,</p>
<pre lang="xml">
<sheet>
    <group name="buttons">
        <sprite name="default" x="" y="" width="" height="" />
        <sprite name="pressed" x="" y="" width="" height="" />
    </group>
</sheet>
</pre>
<p>I liked the idea but I was still concerned with allowing users to access nodes directly with XPath like so,</p>
<pre lang="java">
xml.selectNode("/sheet/buttons/default");
</pre>
<p>But he insisted that users will most likely be concerned with importing the whole sprite sheet configuration and then accessing nodes with my API, and I can also provide an API for addressing sprites similar to XPath. So, to keep story short, I took his suggestions in and now names are exported as attributes.</p>
<p>This brought my attention back to my original problem. If I keep names as attributes then there is no problem with illegal characters! Well, at least in XML configuration. Other configuration formats that will be implemented in the future may still have a problem with illegal characters but I'll let it bother me when the time comes to implementing them.</p>
<p>So, at the end of the day, I have changed the way users import sprites, implemented the rename feature to allow users rename sprites in bulk and kept no restrictions on name characters.</p>
