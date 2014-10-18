---
layout: post
title: Spritey Loses Weight
categories:
- Implementation
tags:
- sprite
- rcp
- gef
- sheet
- spritey
- ruby
- rails
- ror
- coding
- convention
- validation
---

I'm happy to say that I'm back on this project and am ready to finish it off. The time I was off has been really good for me. Not only I've learnt a new language and framework but also opened up a new perspective on how one should write applications. You have to have a certain mindset or coding style when developing application in [Ruby on Rails][ror].

One of the valuable things I've learnt about [RoR][ror] was that the reason why it is so great, apart from the fact that it is written and maintained by very smart people, is that it favours [convention over configuration][coc] thus making it very simple and clean. Therefore, all internal code changes that I'm about to make in Spritey will be about defining conventions.

I've also looked, critically, at the usability of this tool and the requirements that I defined a long, very long time ago. The most annoying and useless feature I found in all sprite sheet tools was the graphical representation of a sprite sheet that I blindly copied without giving it any thought. Why do I find it annoying and useless? Well, it occupies most of the space but doesn't add any real value. It gives a user an idea about how a sprite sheet will look like but does the user even care? Sure, a user can select a sprite in a sprite sheet and change properties but the same can be achieved with selecting sprite in a tree. And from the implementation perspective, [GEF][gef] hasn't really met my expectation. Unfortunately, it doesn't handle a large number of `EditParts` and requires a lot of code to integrate a framework-independent model with itself. Needless to say, I dropped the requirement for graphical representation of a sprite sheet. Overhead associated with implementing it overweighted the usefulness of this feature.

Another feature that I dropped was the property sheet. Again, I found no strong use cases for displaying properties. In fact, renaming a bulk of sprites via a property sheet is not what anyone would want to do.

After striping these two features off I was left with a very naked application. Implementing this tool as an [RCP][rcp] application became pointless and bulky, therefore, I decided to convert this application into a wizard, a single dialog with four steps. Step one, define sprite sheet properties. Step two, add sprites to a sprite sheet. Step three, rename sprites in bulk. Step four, save sprite sheet to a disk. Nice and simple!

Facelift isn't the only change I'm going to do with Spritey. I was very unhappy about how I handled validation failures in my RCP so I decided to refactor it while applying a coding by convention mentality. The reason why the old approach was so convoluted was because I wanted to make validators generic, therefore, making them unaware of which property they were validating. I had the ability to retrieve a message and an error code after validation failed but if I wanted to handle multiple failures I had to write a massive switch statement in order to identify which  validation has failed before displaying an appropriate error message that came from RCP resource messages, messages that came from validators have actually never been used.

The ideal approach is to have the validation to return a property specific internationalized string which can be displayed directly to a user. To achieve this the property ids and the validation error codes were turned into strings. Doing this also improved readability and debugging. The convention now is the property id and validation error code have to be an upper case. Validation message comes from resource messages in the core. The resource key is a combination of property id and validation error code separated with an underscore. For example, if id of the name property is `NAME` and validation error code of a "not a string" is `NOT_STRING` then the resource key for the validation message is `NAME_NOT_STRING`. So, now when validation fails, model retrieves the error code and concatenates it with id of the property being validated to get the key of the resource string. It then gets the message from the resource bundle and stores it in the exception.

[ror]: http://rubyonrails.org "Ruby On Rails"
[coc]: http://en.wikipedia.org/wiki/Convention_over_configuration "Convention Over Configuration"
[rcp]: http://wiki.eclipse.org/index.php/Rich_Client_Platform "Eclipse Rich Client Platform"
[gef]: http://www.eclipse.org/gef "Eclipse Graphical Editing Framework"
