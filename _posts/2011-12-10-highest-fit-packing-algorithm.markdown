---
layout: post
title: Highest Fit Packing Algorithm
categories:
- Implementation
tags:
- sprite
- packer
- sheet
- spritey
- fit
- highest
- packing
- algorithm
---
<p>This algorithm is something I'm very proud of. When I started doing Spritey I didn't know if I would be able to write a packing algorithm that can compete with other packing tools. But after comparing the results with my favourite <a href="http://spritesheetpacker.codeplex.com/" target="_blank">Sprite Sheet Packer</a> I was amazed to see that Spritey does not only pack sprites faster but it's also  more efficient with a larger number of sprites. Here are just a few examples of what Spritey can do. ;)</p>
<p>[caption id="attachment_565" align="aligncenter" width="130" caption="Sprites: 9 Time: 0.01s Size: 130x120"]<a href="http://bykovskyy.com/spritey/blog/wp-content/uploads/2011/12/spritey_1.png"><img class="size-full wp-image-565   " title="spritey-1" src="http://bykovskyy.com/spritey/blog/wp-content/uploads/2011/12/spritey_1.png" alt="" width="130" height="120" /></a>[/caption]</p>
<p>[caption id="attachment_566" align="aligncenter" width="300" caption="Sprites: 512 Time: 0.034s Size: 368x368"]<a href="http://bykovskyy.com/spritey/blog/wp-content/uploads/2011/12/spritey_2.png"><img class="size-medium wp-image-566" title="spritey-2" src="http://bykovskyy.com/spritey/blog/wp-content/uploads/2011/12/spritey_2-300x300.png" alt="" width="300" height="300" /></a>[/caption]</p>
<p>[caption id="attachment_567" align="aligncenter" width="300" caption="Sprites: 720 Time: 0.025s Size: 1416x1012"]<a href="http://bykovskyy.com/spritey/blog/wp-content/uploads/2011/12/spritey_3.png"><img class="size-medium wp-image-567" title="spritey-3" src="http://bykovskyy.com/spritey/blog/wp-content/uploads/2011/12/spritey_3-300x214.png" alt="" width="300" height="214" /></a>[/caption]</p>
<p>[caption id="attachment_568" align="aligncenter" width="300" caption="Sprites: 1241 Time: 0.045s Size: 1544x1012"]<a href="http://bykovskyy.com/spritey/blog/wp-content/uploads/2011/12/spritey_4.png"><img class="size-medium wp-image-568" title="spritey-4" src="http://bykovskyy.com/spritey/blog/wp-content/uploads/2011/12/spritey_4-300x196.png" alt="" width="300" height="196" /></a>[/caption]</p>
<p>So what makes Spritey so fast? Well, I think it's the utilization of a "free zones" technique that I used in my previous <a href="http://bykovskyy.com/spritey/blog/2010/08/flow-strategy-vs-first-fit-strategy/" target="_self">First Fit Algorithm</a>. Free zones are calculated and stored in such a way that the calculation of where to put sprite is instantaneous unless sheet needs to be resized.</p>
<p>The algorithm itself is very simple. First, it sorts sprites by their width in descending order, thus making widest sprites go first. Then a location where to put sprite is calculated by checking if sprite fits into any of the free zones. When the location is found the sprite is set to that location and free zones are recalculated and sorted so that the highest and left most zones are first. If the location is not found the size is expanded by first trying to expand the highest expandable zone. If no such zone exists then either width or height is expanded depending on which one is smaller.</p>
