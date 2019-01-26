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

###License
BigDataScript is open source.

