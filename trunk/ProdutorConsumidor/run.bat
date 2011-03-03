set CLASSPATH=%CLASSPATH%;.\bin;.\lib\jade.jar;
start java jade.Boot -gui
pause
start java jade.Boot -container buffer:jade.exemplos.Buffer
start java jade.Boot -container produtor:jade.exemplos.Produtor
start java jade.Boot -container consumidor:jade.exemplos.Consumidor

