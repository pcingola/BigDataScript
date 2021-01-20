# Download & Install
Binary distributions are available for Linux and OS.X.

<a class="btn btn-large btn-primary" href="https://github.com/pcingola/BigDataScript/blob/master/distro/bds_Linux.tgz?raw=true">Download Linux</a>
<a class="btn btn-large btn-primary" href="https://github.com/pcingola/BigDataScript/blob/master/distro/bds_Darwin.tgz?raw=true">Download OS.X</a>

###Install
Uncompress the binary at your `HOME` directory
```
cd 
tar -xvzf path/to/bds_*.tgz 
```

**Requirements:** In order to run BigDataScript, you need `Java 1.11`.

**Note:** `bds`'s directoy `.bds` is 'hidden', since the name starts with a dot.
Add `bds`'s directory to your `PATH`, by adding the following line to your `.bashrc` or `.bash_profile`
```
export PATH=$PATH:$HOME/.bds
```

**Mesos support (Optional):**
If you intend to use `bds` with mesos, you must install Mesos' native library in `$HOME/.bds/lib`.
For example on a Mac that would be something like
```
cp /usr/local/Cellar/mesos/*/lib/libmesos.dylib $HOME/.bds/lib
```

###Installing from source
The source code is available at GitHub, here we show how to compile and install.

**Requirements:** In order to complile BigDataScript, you need

- [Java](http://java.com) version 11 or higher
- [Go](http://golang.org/) version 1.0 or higher
- [Ant](http://ant.apache.org/) version 1.7 or higher


The source code is available on [Github](https://github.com/pcingola/BigDataScript).
Download the project as follows:
				
				
```
# Clone repository
$ git clone https://github.com/pcingola/BigDataScript.git
```

Once you downloaded the project, you can install using the `install.sh` script:
```
# Run 'install script
cd BigDataScript
./scripts/install.sh
```
				
Then you should add `bds` to your PATH. Edit your `.bashrc` (or `.bash_profile`) and add the following lines
```
# BigDataScript
export PATH=$PATH:$HOME/.bds/
```
				
**Optional:** If you have root access, you can do the following (to get `bds` available everywhere in your system)
```
su -
cd /usr/bin/
ln -s /home/your_user/.bds/bds
```

### Build Jar: Solving missing Java libraries

These are used to build the JAR file

**Build executable jar with dependencies**

Jar with dependencias in directory 'target', e.g. `target/bds-2.3-jar-with-dependencies.jar`
```
cd mvn
mvn clean assembly:assembly
```

### Creating manual pages

Maual pages are written in Markdown (`*.md`) and HTML is created using `mkdocs` package.
The source Markdown documents are located in `manual/docs/`.

The GitHub pages are published from `master` branch, direcotory `/docs`, so manual pages are created in a sub-directory (`docs/manual/site`)

Create a virtual environment for `mkdocs`
```
mkdir $HOME/bds_docs
cd $HOME/bds_docs
virtualenv -p python3 .

# Activate virtualenv and install mkdocs
. bin/activate
pip install mkdocs

# Create links to bds project.
# Here we assume the source code is at `$HOME/workspace/BigDataScript`
BDS_SRC="$HOME/workspace/BigDataScript"
ln -s $BDS_SRC/manual/mkdocs.yml  # Main configuration file for mkdocs
ln -s $BDS_SRC/manual/docs        # Source markdown for manual pages
ln -s $BDS_SRC/docs/manual/site   # GitHub pages are in 'docs' directory
```

Once the virtual environment for `mkdocs` is set you can run:
```
mkdocs serve    # Change pages and see result in your local bowser
```

or

```
mkdocs build    # Create manual pages

cd $BDS_SRC
./git/commit    # Publish manual pages (if in master)
```

###License
BigDataScript is open source.

