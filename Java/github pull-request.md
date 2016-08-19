# How to pull project on github?

- 首先`fork`一个项目
- 把`fork`过去的项目也就是你的项目`clone`到你的本地
- 在命令行运行`git branch develop`来创建一个新分支
- 运行`git checkout develop`来切换到新分支
- 运行`git remote add upstream https://github.com/numbbbbb/the-swift-programming-language-in-chinese.git`把原库添加为远端库
- 运行`git remote update`更新
- 运行`git fetch upstream gh-pages`拉取原远程库的更新到本地
- 运行`git rebase upstream/gh-pages`将我的更新合并到你的分支

这是一个初始化流程，只需要做一遍就行，之后一直在`develop`分支进行修改。
如果修改过程中原库有了更新，请重复6、7、8步。
修改之后，首先`push`到`fork`出来的库，然后登录`GitHub`，在该库的首页可以看到一个`pull request`按钮，点击它，填写一些说明信息，然后提交即可。
