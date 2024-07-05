# Radio Info

This program lists the all the radio channels and programs for each channel from Sveriges Radio's API using XML and shows them in a GUI. The program updates the lists at start and once every hour.

## Compile

```console
mvn package
```

## Run

```console
java -target/RadioInfo-1.0-jar-with-dependencies.jar
```

## Usage

Click on a channel in the channel list to view program list. Click on a program in the program list to view program information.

To update lists manually click Options->Update. 

To exit program click Program->Exit.
