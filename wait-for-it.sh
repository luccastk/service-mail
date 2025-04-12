#!/bin/sh
# Espera até que o serviço esteja disponível na URL
HOST=$1
PORT=$2
shift 2
cmd="$@"

echo "Aguardando $HOST:$PORT..."
until nc -z -v -w30 $HOST $PORT; do
  echo "$HOST:$PORT ainda não está disponível. Tentando novamente..."
  sleep 5
done

echo "$HOST:$PORT está disponível!"
exec $cmd