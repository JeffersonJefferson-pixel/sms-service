bootstrap:
	docker-compose -f bootstrap.yml up -d

db:
	docker-compose -f db.yml up -d

createdb:
	docker exec -it sms-postgres createdb --username=postgres --owner=postgres sms-service

app:
	docker-compose -f app.yml up -d

.PHONY: bootstrap db createdb app