//example
BOT1_SORCE = ./Bots/bot1.cpp
BOT2_SORCE = ./Bots/bot2.cpp
BOT3_SORCE = ./Bots/bot3.cpp

BOT1_EXEC = ./Bots/executables/bot1
BOT2_EXEC = ./Bots/executables/bot2
BOT3_EXEC = ./Bots/executables/bot3

REF = ./SummerChallenge2024Olymbits/target/spring-2024-olympics-1.0-SNAPSHOT.jar

# docker rulls (run outside container)
build_docker :
	docker compose build

run_docker:
	docker compose up -d

run:
	docker exec -it olymbits bash -c "cd olymbits && exec bash";

# referee setup rulls (run inside container)
ref: npm jar 

jar :
	cd SummerChallenge2024Olymbits && mvn package;

npm:
	cd SummerChallenge2024Olymbits/src/main/resources && npm install;
	cd SummerChallenge2024Olymbits/src/main/resources && npm run build;

# run games rull (run inside container)


play : 
	java -jar $(REF) -p1 $(BOT1_EXEC) -p2 $(BOT2_EXEC) -p3 $(BOT3_EXEC) -s

play_hide :
	java -jar $(REF) -p1 $(BOT1_EXEC) -p2 $(BOT2_EXEC) -p3 $(BOT3_EXEC)


compile: 
	g++ $(BOT1_SORCE) -o $(BOT1_EXEC)
	g++ $(BOT2_SORCE) -o $(BOT2_EXEC)
	g++ $(BOT3_SORCE) -o $(BOT3_EXEC)