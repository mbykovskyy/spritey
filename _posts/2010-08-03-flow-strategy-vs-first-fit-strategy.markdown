---
layout: post
title: Flow Strategy vs. First Fit Strategy
categories:
- Research and Design
- Implementation
tags:
- sprite
- packer
- sheet
- strategy
- flow
- first
- fit
- organizer
---

In my previous post I talked about researching and implementing the flow strategy as a default strategy for sprite organizer. When I started implementing it I found a better strategy called the First Fit Strategy and decided to abandon the flow strategy all together.

<h3>Flow Strategy</h3>
<h4>Approach 1</h4>

Sprites are positioned in a row from left to right. When a sprite doesn't fit into the remaining space of a current row it's wrapped onto a new row.  Row's inital size is equal to the sprite sheet width. A sprite is checked against how much space is left in a row. If the sprite fits in, the sprite width is subtracted from the row's width and the result is cached for the next sprite.  When a sprite does not fit into what ever is left in a row, it's moved down to the beginning of a new row.

The new row is specified by the "baseline", which is calculated by finding the highest sprite in a row and then adding its height to the cached baseline. The image below illustrates the end result of this strategy, the grey areas indicate wasted space and red lines represent "baselines".

<a href="http://bykovskyy.com/spritepacker/blog/wp-content/uploads/2010/07/flow-strategy-approach1.png"><img class="aligncenter size-medium wp-image-407" title="flow-strategy-approach1" src="http://bykovskyy.com/spritepacker/blog/wp-content/uploads/2010/07/flow-strategy-approach1-300x192.png" alt="" width="300" height="192" /></a>

This is probably the easiest strategy to implement, but it's very inefficient and requires a lot of manual arrangement.
<h4>Pros</h4>
<ol>
<li>Easy to implement. Theoretically, there're only a few computations. Subtract sprite width from row's remaining space and add the highest sprite width to the "baseline".</li>
<li>Fast. There're no iterations as all values can be cached.</li>
</ol>
<h4>Cons</h4>
<ol>
<li>Very inefficient. Because sprites are positioned one after another in a flow, a lot of space is wasted.</li>
</ol>
<h4>Approach 2</h4>

This is an improvement on the first approach. Sprites are still positioned in a flow from left to right but this time they attach themselves to sprites above them.

<a href="http://bykovskyy.com/spritepacker/blog/wp-content/uploads/2010/07/flow-strategy-approach2.png"><img class="aligncenter size-medium wp-image-409" title="flow-strategy-approach2" src="http://bykovskyy.com/spritepacker/blog/wp-content/uploads/2010/07/flow-strategy-approach2-300x204.png" alt="" width="300" height="204" /></a>

I'm not going to theorise about how to implement it, because I haven't found a "decent" way to shift sprites up, ;) but by trying to implement this approach I found a better strategy called the First Fit Strategy.
<h3>First Fit Strategy</h3>

I arrived to this strategy while looking at the second approach of the flow strategy. As it's name suggests this strategy tries to put each sprite into first smallest area it finds by dividing empty area into zones.  Initially, there's only one empty area covering the whole sprite sheet.

Sprites are tested against empty zones. If sprite fits a particular zone its bounds are subtracted from all intersecting zones, generating largest remaining rectangles. These new zones are then sorted in ascending order and redundant zones i.e. zones completely covered by other zones, are removed. The green areas in the image below illustrate two generated zones as a result of subtraction.

<a href="http://bykovskyy.com/spritepacker/blog/wp-content/uploads/2010/07/first-fit-strategy-free-zones.png"><img class="aligncenter size-medium wp-image-414" title="first-fit-strategy-free-zones" src="http://bykovskyy.com/spritepacker/blog/wp-content/uploads/2010/07/first-fit-strategy-free-zones-300x286.png" alt="" width="300" height="286" /></a>

Say, a new sprite is added and the strategy decides that the area under the first sprite is the best fit. The second sprite will be positioned like so.

<a href="http://bykovskyy.com/spritepacker/blog/wp-content/uploads/2010/07/first-fit-strategy-free-zones2.png"><img class="aligncenter size-medium wp-image-419" title="first-fit-strategy-free-zones2" src="http://bykovskyy.com/spritepacker/blog/wp-content/uploads/2010/07/first-fit-strategy-free-zones2-300x286.png" alt="" width="300" height="286" /></a>

The sprite area will be subtracted from both zones generating five new zones. Two zones will eventually be removed as they are overlapped by bigger zones resulting in only three zones.

<a href="http://bykovskyy.com/spritepacker/blog/wp-content/uploads/2010/07/first-fit-strategy-free-zones32.png"><img class="aligncenter size-full wp-image-424" title="first-fit-strategy-free-zones3" src="http://bykovskyy.com/spritepacker/blog/wp-content/uploads/2010/07/first-fit-strategy-free-zones32.png" alt="" width="301" height="576" /></a>
<h4>Pros</h4>
<ol>
<li>Quite efficient. It is relatively efficient as it tries to fill all available spaces in a sprite sheet by always filling in the smallest areas first.</li>
<li>Relatively fast. I don't have any stats to be 100% sure about it's speed, but in theory it should have a <a href="http://en.wikipedia.org/wiki/Normal_distribution" target="_blank">normal distribution</a>. Number of free zones will increase as a number of sprites increase but then as less and less space become available the number of free zones will start to decrease, resulting in the increased speed.</li>
</ol>
<h4>Cons</h4>
<ol>
<li>Quite complex. It's not the most complicated algorithm in the world but still it requires some explaining.</li>
</ol>
