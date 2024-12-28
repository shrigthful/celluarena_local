BOT1_SORCE = ./Bots/bot5.cpp
BOT2_SORCE = ./Bots/botWait.cpp

BOT1_EXEC = ./Bots/executables/player1
BOT2_EXEC = ./Bots/executables/player2


REF = ./WinterChallenge2024-Cellularena/target/winter-2024-sprawl-1.0-SNAPSHOT.jar

# docker rulls (run outside container)
build :
	docker compose build

run:
	docker compose up -d

down : 
	docker compose down

re :  down build run

clean : down
	docker rmi cellularean
	docker volume rm cellularena_cgvolcell 

connect:
	docker exec -it cellularena bash -c "cd cellularena && exec bash";

# referee setup rulls (run inside container)
ref: npm jar 

jar :
	cd WinterChallenge2024-Cellularena && mvn package;

npm:
	cd WinterChallenge2024-Cellularena/src/main/resources && npm install;
	cd WinterChallenge2024-Cellularena/src/main/resources && npm run build;

# run games rull (run inside container)

# -3314908149052506000
# -6085617765562378000
play : compile
	java -jar $(REF) -p1 $(BOT1_EXEC) -p2 $(BOT2_EXEC) -s

seed: compile
	java -jar $(REF) -p1 $(BOT1_EXEC) -p2 $(BOT2_EXEC) -s -d =-3314908149052506000

play_hide : compile
	java -jar $(REF) -p1 $(BOT1_EXEC) -p2 $(BOT2_EXEC) -s


quick_play:
	java -jar $(REF) -p1 $(BOT1_EXEC) -p2 $(BOT2_EXEC)

psy:
	cd ./psyleague && psyleague show
	
psy_run:
	cd ./psyleague && psyleague run

psy_add_all:
	cd ./psyleague && bash addPlayers.sh

compile: 
	g++ -fsanitize=address -g  $(BOT1_SORCE) -o $(BOT1_EXEC)
	g++ -fsanitize=address -g  $(BOT2_SORCE) -o $(BOT2_EXEC)