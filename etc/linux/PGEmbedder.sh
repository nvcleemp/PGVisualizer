if [ -L $0 ]
then
   PATHNAME=`readlink $0`
else
   PATHNAME=$0
fi
DIRNAME=`dirname $PATHNAME`

export CLASSPATH=.:PG.jar
for JAR in `ls $DIRNAME/lib/*.jar`
do
  CLASSPATH=$JAR:$CLASSPATH
done

java be.ugent.caagt.pg.Starter -p embedder