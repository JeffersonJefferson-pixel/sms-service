bootstrap:
	docker-compose -f bootstrap.yml up -d

app:
	docker-compose -f app.yml up -d

.PHONY: bootstrap app