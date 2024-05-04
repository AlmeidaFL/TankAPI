user := test
password := test_password
space_name := pirulito

brun:
	./gradlew build
	./gradlew run


create_space: 
	curl --cacert "$$(mkcert -CAROOT)/rootCA.pem" -u $(user):$(password) -d '{"name": "$(space_name)", "owner": "$(user)"}' -H 'Content-type: application/json' https://localhost:4567/spaces

create_user: 
	curl --cacert "$$(mkcert -CAROOT)/rootCA.pem" -d '{"username":"$(user)","password":"$(password)"}' -H 'Content-Type: application/json' https://localhost:4567/users
