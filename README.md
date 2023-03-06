useful pact broker documentation here -> https://docs.pact.io/implementation_guides/jvm/provider/junit#download-pacts-from-a-pact-broker

To enable publishing of results, set the Java system property or environment variable pact.verifier.publishResults to true.
This can be included in the env variables within the IDE or in the mvn verify -Dpact.verifier.publishResults=true
