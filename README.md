# Spritey
Spritey is an open-source sprite sheet creator, written in Java with SWT and
JFace front-end.

This tool is distributed under GNU GPL version 3, which can be found in the file
COPYING.

## Run via command-line
### On Linux x86_64
- Compile core classes
```
cd ./spritey.core
find -name "*.java" -not -path "*/test/*" > sources.txt
javac -cp ./libs/java-xmlbuilder-0.3.jar -d ./bin @sources.txt
```
- Copy `messages.properties` file to `bin` directory
- Compile UI classes
```
cd ./spritey.ui
find -name "*.java" -not -path "*/test/*" > sources.txt
javac -cp ./libs/swt-3.7.1-linux-gtk-x86.jar:./libs/org.eclipse.equinox.common_3.6.0.v20100503.jar:./libs/org.eclipse.jface_3.6.0.I20100601-0800.jar:./libs/org.eclipse.osgi_3.6.0.v20100517.jar:./libs/org.eclipse.core.commands_3.6.0.I20100512-1500.jar:../spritey.core/bin -d ./bin @sources.txt
```
- Copy `messages.properties` file and `icons` directory to `bin` directory
- Run application
```
cd ./spritey.ui
java -cp ./libs/swt-3.7.1-linux-gtk-x86_64.jar:./libs/org.eclipse.equinox.common_3.6.0.v20100503.jar:./libs/org.eclipse.jface_3.6.0.I20100601-0800.jar:./libs/org.eclipse.osgi_3.6.0.v20100517.jar:./libs/org.eclipse.core.commands_3.6.0.I20100512-1500.jar:../spritey.core/bin:./bin spritey.ui.Application
```
