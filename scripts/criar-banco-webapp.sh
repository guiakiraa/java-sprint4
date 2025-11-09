#!/bin/bash

export RESOURCE_GROUP_NAME="rg-triasprint4"
export WEBAPP_NAME="triasprint4-mottu"
export APP_SERVICE_PLAN="planTriasSprint4"
export LOCATION="brazilsouth"
export RUNTIME="JAVA:17-java17"
export SERVER_NAME="sqlserver-triasprint4"
export USERNAME="admsql"
export PASSWORD="Fiap@2tdsvms"
export DBNAME="triadb"

az group create --name $RESOURCE_GROUP_NAME --location $LOCATION
az sql server create -l $LOCATION -g $RESOURCE_GROUP_NAME -n $SERVER_NAME -u $USERNAME -p $PASSWORD --enable-public-network true
az sql db create -g $RESOURCE_GROUP_NAME -s $SERVER_NAME -n $DBNAME --service-objective Basic --backup-storage-redundancy Local --zone-redundant false
az sql server firewall-rule create -g $RESOURCE_GROUP_NAME -s $SERVER_NAME -n liberaGeral --start-ip-address 0.0.0.0 --end-ip-address 255.255.255.255

az appservice plan create \
  --name "$APP_SERVICE_PLAN" \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --location "$LOCATION" \
  --sku F1 \
  --is-linux

az webapp create \
  --name "$WEBAPP_NAME" \
  --resource-group "$RESOURCE_GROUP_NAME" \
  --plan "$APP_SERVICE_PLAN" \
  --runtime "$RUNTIME"
