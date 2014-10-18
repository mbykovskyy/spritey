---
layout: post
title: Requirements Specification
categories:
- Research and Design
- Requirements
tags:
- sprite
- spritepacker
- spritesheet
- user story
- Requirements
- specification
- sheet
- texture
- imagepacker
---

Before I go on to the design I thought it would be a good idea to document the requirements. It might not be a super important activity to do in a simple solo project like this but sometimes I find myself forgetting some crucial details that later I kick myself wishing I documented them.

So, I'll start with some high level [user stories][user_story] and then describe some scenarios to illustrate how the user interface should work.

# User stories
As a user I can:

0. Create a sprite sheet.
0. Set sprite sheet background color.
0. Set sprite sheet dimensions.
0. Crop sprite sheet.
0. Add a single sprite to a sprite sheet.
0. Add a group of sprites to a sprite sheet.
0. Remove selected sprites.
0. Select a single sprite.
0. Select a group of sprites.
0. Set sprite position.
0. Set sprite class.
0. Set sprite subclass.
0. Observe sprite dimensions.
0. Observe sprite sheet information such as dimensions, coverage etc. in a status bar.
0. Move sprite around sprite sheet with a mouse.
0. Export sprite sheet.

# Starting application
The application starts with an empty workspace. The sprite list view and sprite properties view are visible but disabled.

# Creating a sprite sheet
A new sprite sheet is created via the *File* menu. The user clicks on the *New* menu item and a sprite sheet wizard appears on the screen. The user now has two options:

0. Fill in the sprite sheet properties such as background color and dimensions then click *Ok*. A new empty sprite sheet is displayed in the graphical editor. The sprite list and sprite properties views along with "Add new sprite" and "Add sprites" buttons become enabled. The "Remove selected sprites" button remains disabled.
0. Click *Cancel* to return to the main view.

# Changing sprite sheet properties
Sprite sheet properties can be changed through the *Spritesheet* menu or a pop-up menu by right-clicking on a sprite sheet. The user clicks on the *Properties* menu item and a dialog box appears. The user now has two options:

0. Change the sprite sheet properties then click *Save* to save changes. Once the changes are saved the sprite sheet in the graphical editor is redrawn.
0. Click Cancel to return to the main view.

# Add a single sprite to a sprite sheet
A user can add a new sprite to a sprite sheet by clicking on the "Add new sprite" button in the tool-box pane located on the right. When a user does so a dialog box appears where a user has two options:

0. Navigate through directories to locate the image user wants to import and press *Ok*. After pressing *Ok*, the selected image will be loaded and displayed on a sprite sheet. The new sprite is selected in both the graphical editor and the sprite list. The sprite properties view displays sprite properties. The "Remove selected sprites" button is enabled. The position of a newly added sprite will be determined in one of the following ways:
	0.  If the "Auto-arrange sprites" option is set, the new sprite will be added to a sprite sheet and the position of every sprite will be recalculated.
	0. If the "Auto-arrange sprites" option is NOT set, the new sprite will be positioned after the last sprite.
0. Click Cancel to return to the main window.

# Add a group of sprites to a sprite sheet
A user can add a group of sprites to a sprite sheet by clicking on the "Add sprites" button in the tool-box pane on the right. When a user does so a dialog box appears where a user has two options:

0. Navigate to the directory with sprites the user wants to add to a sprite sheet. After the user presses *Ok*, all sprites under that directory are loaded and displayed in a graphical editor. New sprites are selected in both the graphical editor and the sprite list. The sprite properties view is cleared and the "Remove selected sprites" button is enabled. The position of new sprites will be determined in one of the following ways:
	0. If the "Auto-arrange sprites" option is set, new sprites will be added to a sprite sheet and the position of every sprite will be recalculated.
	0. If the "Auto-arrange sprites" option is NOT set, new sprites will be positioned after the last sprite.
0. Click Cancel to return to the main window.

# Remove selected sprites
A user can remove sprites by selecting them in either the graphical editor or  the sprite list and then clicking the "Remove selected sprites" button in the tool-box pane located on the right. Sprites are removed from the graphical editor and the sprite list. The sprite properties view is cleared and the "Remove selected sprites" button is disabled since sprites are no longer selected.

# Sprite selection
Sprites can be selected in either the graphical editor or the sprite list. When a sprite is selected in one of the views the corresponding sprite in the other view is also selected. The "Remove selected sprites" button is enabled. If a single sprite is selected, the sprite properties view displays the properties of that sprite. When multiple sprites are selected, the sprite properties view becomes blank. Multiple sprites can be selected by a Ctrl+Left-click or by left-clicking on an empty space in the graphical editor and dragging the mouse over sprites.

# Set sprite position
Sprite position can be manually set if the "Auto-arrange sprites" option in NOT set. To set sprite's position the user will type the new coordinates in the sprite properties view. Graphical editor will automatically refresh to reflect the change.

# Set sprite class
A user can set sprite's class in the sprite properties view. The sprite list/tree will refresh to reflect the change. The sprite sheet view will remain unchanged. If the new sprite class already exists then the sprite list item will be moved to that class group otherwise a new class group will be created. If the user leaves the sprite's class blank an error message will be  displayed.

# Set sprite subclass
A user can set sprite's subclass in the sprite properties view. The sprite list/tree will refresh to reflect the change. The sprite sheet view will remain unchanged. If the user leaves the subclass field blank an error message will be displayed.

# Manipulating sprite in a sprite sheet view
When "Auto-arrange sprites" option is NOT set the user will be able to move sprites around sprite sheet with a mouse. If "Snap to sprites" option is set the moving sprite will snap to other sprites, otherwise the selected sprite will move freely and may overlap others. Sprite properties view will update sprite coordinates fields to reflect the change.

# Manipulating sprites in a sprite list/tree view
In a sprite tree view a user can select and move sprite list items. When a sprite is selected in the sprite tree the corresponding sprite is selected in a graphical editor (sprite sheet view) and sprite properties are displayed in a sprite properties view.

When sprite is moved the sprite class will change to the class the sprite was placed under. Sprite properties view will reflect the change. Sprite sheet view will remain unchanged.

[user_story]: http://en.wikipedia.org/wiki/User_story "User Story"
