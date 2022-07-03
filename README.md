# Babashka book

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## Build

``` shell
$ script/compile
```

This uses `asciidoctor` to spit out an HTML file into the `gh-pages` directory.
To install `asciidoctor`, check the documentation [here](https://asciidoctor.org/).

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

After cloning this repo to a new dir/computer:

``` shell
git fetch origin gh-pages
git worktree prune
git worktree add gh-pages gh-pages
```

## License

Copyright © 2020-2021 Michiel Borkent

Licensed under [CC BY-SA 4.0](https://creativecommons.org/licenses/by-sa/4.0).
