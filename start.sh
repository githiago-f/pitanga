HOME=$(pwd)
DATE=$(date +"%Y/%m/%d")
LOG_FILE_PATH=$HOME/.logs/$DATE

mkdir -p $LOG_FILE_PATH

java -jar ./app.jar > $LOG_FILE_PATH/logs.txt &
