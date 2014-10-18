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
<p>Before I go on to the design I thought it would be a good idea to document the requirements. It might not be a super important activity to do in a simple solo project like this but sometimes I find myself forgetting some crucial details that later I kick myself wishing I documented them.</p>
<p>So, I'll start with some high level <a href="http://en.wikipedia.org/wiki/User_story" target="_blank">user stories</a> and then describe some scenarios to illustrate how the user interface should work.</p>
<h3>User stories</h3>
<p>As a user I can:</p>
<ol>
<li>Create a sprite sheet.</li>
<li>Set sprite sheet background color.</li>
<li>Set sprite sheet dimensions.</li>
<li>Crop sprite sheet.</li>
<li>Add a single sprite to a sprite sheet.</li>
<li>Add a group of sprites to a sprite sheet.</li>
<li>Remove selected sprites.</li>
<li>Select a single sprite.</li>
<li>Select a group of sprites.</li>
<li>Set sprite position.</li>
<li>Set sprite class.</li>
<li>Set sprite subclass.</li>
<li>Observe sprite dimensions.</li>
<li>Observe sprite sheet information such as dimensions, coverage etc. in a status bar.</li>
<li>Move sprite around sprite sheet with a mouse.</li>
<li>Export sprite sheet.</li>
</ol>
<h3>Starting application</h3>
<p>The application starts with an empty workspace. The sprite list view and sprite properties view are visible but disabled.</p>
<h3>Creating a sprite sheet</h3>
<p>A new sprite sheet is created via the <em>File</em> menu. The user clicks on the <em>New</em> menu item and a sprite sheet wizard appears on the screen. The user now has two options:</p>
<ol>
<li>Fill in the sprite sheet properties such as background color and dimensions then click <em>Ok</em>. A new empty sprite sheet is displayed in the graphical editor. The sprite list and sprite properties views along with "Add new sprite" and "Add sprites" buttons become enabled. The "Remove selected sprites" button remains disabled.</li>
<li>Click <em>Cancel</em> to return to the main view.</li>
</ol>
<h3>Changing sprite sheet properties</h3>
<p>Sprite sheet properties can be changed through the <em>Spritesheet</em> menu or a pop-up menu by right-clicking on a sprite sheet. The user clicks on the <em>Properties</em> menu item and a dialog box appears. The user now has two options:</p>
<ol>
<li>Change the sprite sheet properties then click <em>Save</em> to save changes. Once the changes are saved the sprite sheet in the graphical editor is redrawn.</li>
<li>Click Cancel to return to the main view.</li>
</ol>
<h3>Add a single sprite to a sprite sheet</h3>
<p>A user can add a new sprite to a sprite sheet by clicking on the "Add new sprite" button in the tool-box pane located on the right. When a user does so a dialog box appears where a user has two options:</p>
<ol>
<li>Navigate through directories to locate the image user wants to import and press <em>Ok</em>. After pressing <em>Ok</em>, the selected image will be loaded and displayed on a sprite sheet. The position of a newly added sprite will be determined in one of the following ways:
<ol>
<li> If the "Auto-arrange sprites" option is set, the new sprite will be added to a sprite sheet and the position of every sprite will be recalculated.</li>
<li>If the "Auto-arrange sprites" option is NOT set, the new sprite will be positioned after the last sprite.</li>
</ol>
<p>The new sprite is selected in both the graphical editor and the sprite list. The sprite properties view displays sprite properties. The "Remove selected sprites" button is enabled.</li>
<li>Click Cancel to return to the main window.</li>
</ol>
<h3>Add a group of sprites to a sprite sheet</h3>
<p>A user can add a group of sprites to a sprite sheet by clicking on the "Add sprites" button in the tool-box pane on the right. When a user does so a dialog box appears where a user has two options:</p>
<ol>
<li>Navigate to the directory with sprites the user wants to add to a sprite sheet. After the user presses <em>Ok</em>, all sprites under that directory are loaded and displayed in a graphical editor. The position of new sprites will be determined in one of the following ways:
<ol>
<li>If the "Auto-arrange sprites" option is set, new sprites will be added to a sprite sheet and the position of every sprite will be recalculated.</li>
<li>If the "Auto-arrange sprites" option is NOT set, new sprites will be positioned after the last sprite.</li>
</ol>
<p>New sprites are selected in both the graphical editor and the sprite list. The sprite properties view is cleared and the "Remove selected sprites" button is enabled.</li>
<li>Click Cancel to return to the main window.</li>
</ol>
<h3>Remove selected sprites</h3>
<p>A user can remove sprites by selecting them in either the graphical editor or  the sprite list and then clicking the "Remove selected sprites" button in the tool-box pane located on the right. Sprites are removed from the graphical editor and the sprite list. The sprite properties view is cleared and the "Remove selected sprites" button is disabled since sprites are no longer selected.</p>
<h3>Sprite selection</h3>
<p>Sprites can be selected in either the graphical editor or the sprite list. When a sprite is selected in one of the views the corresponding sprite in the other view is also selected. The "Remove selected sprites" button is enabled. If a single sprite is selected, the sprite properties view displays the properties of that sprite. When multiple sprites are selected, the sprite properties view becomes blank. Multiple sprites can be selected by a Ctrl+Left-click or by left-clicking on an empty space in the graphical editor and dragging the mouse over sprites.</p>
<h3>Set sprite position</h3>
<p>Sprite position can be manually set if the "Auto-arrange sprites" option in NOT set. To set sprite's position the user will type the new coordinates in the sprite properties view. Graphical editor will automatically refresh to reflect the change.</p>
<h3>Set sprite class</h3>
<p>A user can set sprite's class in the sprite properties view. The sprite list/tree will refresh to reflect the change. The sprite sheet view will remain unchanged. If the new sprite class already exists then the sprite list item will be moved to that class group otherwise a new class group will be created. If the user leaves the sprite's class blank an error message will be  displayed.</p>
<h3>Set sprite subclass</h3>
<p>A user can set sprite's subclass in the sprite properties view. The sprite list/tree will refresh to reflect the change. The sprite sheet view will remain unchanged. If the user leaves the subclass field blank an error message will be displayed.</p>
<h3>Manipulating sprite in a sprite sheet view</h3>
<p>When "Auto-arrange sprites" option is NOT set the user will be able to move sprites around sprite sheet with a mouse. If "Snap to sprites" option is set the moving sprite will snap to other sprites, otherwise the selected sprite will move freely and may overlap others. Sprite properties view will update sprite coordinates fields to reflect the change.</p>
<h3>Manipulating sprites in a sprite list/tree view</h3>
<p>In a sprite tree view a user can select and move sprite list items. When a sprite is selected in the sprite tree the corresponding sprite is selected in a graphical editor (sprite sheet view) and sprite properties are displayed in a sprite properties view.</p>
<p>When sprite is moved the sprite class will change to the class the sprite was placed under. Sprite properties view will reflect the change. Sprite sheet view will remain unchanged.</p>
