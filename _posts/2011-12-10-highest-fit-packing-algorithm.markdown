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

This algorithm is something I'm very proud of. When I started working on Spritey I didn't know if I would be able to write a packing algorithm that can compete with other packing tools. But after comparing the results with my favourite [Sprite Sheet Packer][sprite_packer] I was amazed to see that Spritey does not only pack sprites faster but it's also  more efficient with a larger number of sprites. Here are just a few examples of what Spritey can do.

![][sprite_sheet_1]

*Sprites: 9 Time: 0.01s Size: 130x120*

![][sprite_sheet_2]

*Sprites: 512 Time: 0.034s Size: 368x368*

![][sprite_sheet_3]

*Sprites: 720 Time: 0.025s Size: 1416x1012*

![][sprite_sheet_4]

*Sprites: 1241 Time: 0.045s Size: 1544x1012*

So what makes Spritey so fast? Well, I think it's the utilization of a "free zones" technique that I used in my previous [First Fit Algorithm][first_fit_strategy]. Free zones are calculated and stored in such a way that the calculation of where to put sprite is instantaneous unless sheet needs to be resized.

The algorithm itself is very simple. First, it sorts sprites by their width in descending order, thus making widest sprites go first. Then a location where to put sprite is calculated by checking if sprite fits into any of the free zones. When the location is found the sprite is set to that location and free zones are recalculated and sorted so that the highest and left most zones are first. If the location is not found the size is expanded by first trying to expand the highest expandable zone. If no such zone exists then either width or height is expanded depending on which one is smaller.

[sprite_packer]: http://spritesheetpacker.codeplex.com  "Sprite Sheet Packer"
[sprite_sheet_1]: {{site.baseurl}}/assets/images/2011/12/sprite_sheet_1.png "Sprite Sheet"
[sprite_sheet_2]: {{site.baseurl}}/assets/images/2011/12/sprite_sheet_2.png "Sprite Sheet"
[sprite_sheet_3]: {{site.baseurl}}/assets/images/2011/12/sprite_sheet_3.png "Sprite Sheet"
[sprite_sheet_4]: {{site.baseurl}}/assets/images/2011/12/sprite_sheet_4.png "Sprite Sheet"
[first_fit_strategy]: {{site.baseurl}}{% post_url 2010-08-03-flow-strategy-vs-first-fit-strategy %}#first-fit-strategy "First Fit Strategy"
