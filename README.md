# Babashka cookbook

## Build

``` shell
$ asciidoctor-pdf src/book.adoc -o gh-pages/index.html
```

## Release

Files produced by asciidoctor are hosted on Github. This is set up like
described
[here](https://medium.com/linagora-engineering/deploying-your-js-app-to-github-pages-the-easy-way-or-not-1ef8c48424b7):

All the commands below assume that you already have a git project initialized and that you are in its root folder.

```
# Create an orphan branch named gh-pages
git checkout --orphan gh-pages
# Remove all files from staging
git rm -rf .
# Create an empty commit so that you will be able to push on the branch next
git commit --allow-empty -m "Init empty branch"
# Push the branch
git push origin gh-pages
```

Now that the branch is created and pushed to origin, let’s configure the
worktree correctly.

```
# Come back to master
git checkout master
# Add gh-pages to .gitignore
echo "gh-pages/" >> .gitignore
git worktree add gh-pages gh-pages
```

That’s it, you can now build your app as usual with npm run build . If you cd to
the gh-pages folder, you will notice that you are now in the gh-pages branch and if
you go back to the root folder, you will go back to master .

To deploy to Github Pages:

```
cd gh-pages
git add .
git commit -m "update build"
git push
```

## License

<a rel="license"
href="http://creativecommons.org/licenses/by-nc-nd/3.0/deed.en_US"><img
alt="Creative Commons License" style="border-width:0"
src="http://i.creativecommons.org/l/by-nc-nd/3.0/88x31.png" /></a><br /><span
xmlns:dct="http://purl.org/dc/terms/" property="dct:title">This draft of
Babashka Cookbook</span> is licensed under a <a rel="license"
href="http://creativecommons.org/licenses/by-nc-nd/3.0/deed.en_US">Creative
Commons Attribution-NonCommercial-NoDerivs 3.0 Unported License</a>.


Please see the [contribution guide](CONTRIBUTING.md) for how this works for accepting pull requests.

Also, please note that because this is a *No Derivatives* license, you may *not*
use this repository as a basis for creating your own book based on this
one. Technically speaking, this book is open source in the "free as in beer"
sense, rather than "free as in speech."
