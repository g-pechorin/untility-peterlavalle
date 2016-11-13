
# make the dir if needed
if [ ! -d .hugit/ ]; then
  git clone git@github.com:g-pechorin/untility-peterlavalle.git
  mv untility-peterlavalle/.git .hugit
  rm -dfr untility-peterlavalle
fi;

#
git --git-dir=`pwd`/.hugit/ $*
