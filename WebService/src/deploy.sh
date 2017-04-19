# create BUILD directory
rm -r ../build
mkdir ../build

# build Repository service
mkdir -p ../build/Repository/words
cp -r words/repo ../build/Repository/words
mkdir ../build/Repository/META-INF
cd ../build/Repository
mv words/repo/services.xml META-INF/
cd ../../src

# build Search Gateway service
mkdir -p ../build/SearchGateway/words
cp -r words/search ../build/SearchGateway/words
mkdir ../build/SearchGateway/META-INF
cd ../build/SearchGateway
mv words/search/services.xml META-INF/
cd ..

# zip stuff
cd Repository
zip -0 -r Repository.aar ./words ./META-INF -x "*.java"
cd ../SearchGateway
zip -0 -r SearchGateway.aar ./words ./META-INF -x "*.java"