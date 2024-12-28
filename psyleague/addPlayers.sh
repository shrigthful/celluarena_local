bots_dir="../Bots/";
players=$(find $bots_dir -maxdepth 1 -type f | sed 's/\.cpp//')
let "index=1";
for player in $players;
do
    name=$(echo $player | sed 's/..\/Bots\///');
    psyleague bot add BOT_$name -s $player;
    let "index++";
done