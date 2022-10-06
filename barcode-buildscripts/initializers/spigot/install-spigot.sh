#!/usr/bin/env bash
if [ $# -eq 0 ]; then
    echo "버전 인자가 없습니다."
    exit 1
fi

mkdir -p $HOME/spigot-build
pushd $HOME/spigot-build
echo "스피곳 BuildTools 를 다운 중입니다... 버전: $1"
curl https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar -o $HOME/spigot-build/BuildTools.jar

git_autocrlf=`git config --global core.autocrlf`
git config --global --unset core.autocrlf

echo "스피곳 BuildTools 를 이용해 빌드중입니다... 버전: $1"
java -Xmx1500M -jar BuildTools.jar --rev $1 --compile CRAFTBUKKIT,SPIGOT --remapped | grep Installing

git config --global core.autocrlf $git_autocrlf
popd