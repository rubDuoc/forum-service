#!/bin/sh
# Inicio del contenedor.
# En Render el wallet de Oracle llega como archivo secreto de texto (wallet.tar.gz en base64),
# porque los archivos secretos no admiten binarios: aqui se decodifica y se usa como TNS_ADMIN.
# En local no existe ese archivo y se usa la carpeta montada en /app/wallet.
set -e

if [ -f /etc/secrets/wallet.tar.gz.b64 ]; then
  mkdir -p /tmp/wallet
  base64 -d /etc/secrets/wallet.tar.gz.b64 | tar -xz -C /tmp/wallet
  export TNS_ADMIN=/tmp/wallet
fi

exec java -XX:MaxRAMPercentage=75 -jar /app/app.jar
