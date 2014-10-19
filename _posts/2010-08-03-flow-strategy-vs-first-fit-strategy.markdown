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


# Flow Strategy

## Approach 1

Sprites are positioned in a row from left to right. When a sprite doesn't fit into the remaining space of a current row it's wrapped onto a new row.  Row's inital size is equal to the sprite sheet width. A sprite is checked against how much space is left in a row. If the sprite fits in, the sprite width is subtracted from the row's width and the result is cached for the next sprite.  When a sprite does not fit into what ever is left in a row, it's moved down to the beginning of a new row.

The new row is specified by the "baseline", which is calculated by finding the highest sprite in a row and then adding its height to the cached baseline. The image below illustrates the end result of this strategy, the grey areas indicate wasted space and red lines represent "baselines".

![][flow_strategy_approach1]

This is probably the easiest strategy to implement, but it's very inefficient and requires a lot of manual arrangement.

### Pros

0. Easy to implement. Theoretically, there're only a few computations. Subtract sprite width from row's remaining space and add the highest sprite width to the "baseline".
0. Fast. There're no iterations as all values can be cached.

### Cons

0. Very inefficient. Because sprites are positioned one after another in a flow, a lot of space is wasted.

## Approach 2

This is an improvement on the first approach. Sprites are still positioned in a flow from left to right but this time they attach themselves to sprites above them.

![][flow_strategy_approach2]

I'm not going to theorise about how to implement it, because I haven't found a "decent" way to shift sprites up, but by trying to implement this approach I found a better strategy called the First Fit Strategy.

# First Fit Strategy

I arrived to this strategy while looking at the second approach of the flow strategy. As it's name suggests this strategy tries to put each sprite into first smallest area it finds by dividing empty area into zones.  Initially, there's only one empty area covering the whole sprite sheet.

Sprites are tested against empty zones. If sprite fits a particular zone its bounds are subtracted from all intersecting zones, generating largest remaining rectangles. These new zones are then sorted in ascending order and redundant zones i.e. zones completely covered by other zones, are removed. The green areas in the image below illustrate two generated zones as a result of subtraction.

![][first_fit_strategy_free_zones1]

Say, a new sprite is added and the strategy decides that the area under the first sprite is the best fit. The second sprite will be positioned like so.

![][first_fit_strategy_free_zones2]

The sprite area will be subtracted from both zones generating five new zones. Two zones will eventually be removed as they are overlapped by bigger zones resulting in only three zones.

![][first_fit_strategy_free_zones3]

### Pros

0. Quite efficient. It is relatively efficient as it tries to fill all available spaces in a sprite sheet by always filling in the smallest areas first.
0. Relatively fast. I don't have any stats to be 100% sure about it's speed, but in theory it should have a [normal distribution][normal_distribution]. Number of free zones will increase as a number of sprites increase but then as less and less space become available the number of free zones will start to decrease, resulting in the increased speed.

### Cons

0. Quite complex. It's not the most complicated algorithm in the world but still it requires some explaining.

[flow_strategy_approach1]: {{site.baseurl}}/assets/images/2010/07/flow-strategy-approach1.png "Flow Strategy Approach 1"
[flow_strategy_approach2]: {{site.baseurl}}/assets/images/2010/07/flow-strategy-approach2.png "Flow Strategy Approach 2"
[first_fit_strategy_free_zones1]: {{site.baseurl}}/assets/images/2010/07/first-fit-strategy-free-zones1.png "First Fit Strategy Free Zones"
[first_fit_strategy_free_zones2]: {{site.baseurl}}/assets/images/2010/07/first-fit-strategy-free-zones2.png "First Fit Strategy Free Zones"
[first_fit_strategy_free_zones3]: {{site.baseurl}}/assets/images/2010/07/first-fit-strategy-free-zones3.png "First Fit Strategy Free Zones"

[normal_distribution]: http://en.wikipedia.org/wiki/Normal_distribution "Normal Distribution"