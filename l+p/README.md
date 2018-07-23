mvn gatling:execute -Duri=http://localhost:15001/cgi-bin -Dgatling.simulationClass=wechatin.MessageOutboundSimulation
mvn gatling:execute -Duri=http://localhost:15001 -Dgatling.simulationClass=wechatin.MessageInboundSimulation
