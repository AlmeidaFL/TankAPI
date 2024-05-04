user := test
password := test_password
space_name := pirulito

brun:
	./gradlew build
	./gradlew run

create_space:
	curl -u $(user):$(password) -d '{"name": "$(space_name)", "owner": "$(user)"}' -H 'Content-type application/json' http://localhost:4567/spaces

create_user:
	curl -d '{"username":"$(user)","password":"$(password)"}'-H 'Content-Type: application/json' http://localhost:4567/users
