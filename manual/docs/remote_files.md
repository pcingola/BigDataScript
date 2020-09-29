# Remote files (Amazon S3, Http, etc.) 
Often applications need to run tasks on remote data files, `bds` can transparently handle remote data dependencies

In many cases data files may reside in non-local file systems, such as `HTTP` or Amazon's `S3` object storage.
Fortunately `bds` can transparently handle remote dependencies, download the input files and upload the results without you having to write a single line of code.


**Example 1:** In this example, the remote file `index.html` is remote input file to the `task`.
Obviously `index.html` is hosted on GitHub's servers, thus not available on the computer where the script is running.
Before the command (`cat`) is executed, the remote file is transparently downloaded by `bds`.


```
in  := 'http://pcingola.github.io/BigDataScript/index.html'
out := 'tmp.html'

task( out <- in ) sys cat $in > $out
```

Notice that: 
<ol>
<li> there is no code for downloading the remote file (`index.html`) in the script;
<li> the file is downloaded on the processing node performing the task, which may differ from the node running the script (e.g. if it is running on a cluster);
<li> task dependencies are verified without downloading data, so the task, as well as the corresponding download / upload operations, are only performed if required;
<li> if the file is required in the future, `bds` checks if the local (cached) copy is still valid, and uses the cached file if possible (saving bandwith and time).
</ol>

**Example 2:** The following example is slightly more complicated, the input ('index.html') is processed (`cat` and `echo` commands) and the results are stored in an `Amazon S3` object.
Once more, notice that `bds` transparently takes care of downloading the file and then uploading the output to Amazon's S3.


```
in  := 'http://pcingola.github.io/BigDataScript/index.html'
out := 's3://pcingola.bds/test_remote_12.txt'

task( out <- in ) {
sys cat $in > $out
sys echo "This line is appended to the file" >> $out
}
```


