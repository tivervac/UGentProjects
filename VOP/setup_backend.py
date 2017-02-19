#!/usr/bin/python
#
# Small script to setup the database and initialize a root admin account.
# You can also just execute the SQL files in the "ddl" folder alphabetically
# and manually insert an admin account.
#
# Execute it using "python setup_backend.py setup_db add_admin"
# Alternatively, if you have made this file executable, you can use "./setup_backend.py setup_db add_admin"
# For information on parameters, use the "--help" argument.
#
# Requirements:
# - postgresql-client (apt-get)
# - libpq-dev (apt-get)
# - libffi-dev (apt-get)
# - python (apt-get)
# - python-dev (apt-get)
# - python-pip (apt-get)
# - psycopg2 (pip install)
# - click (pip install)
# - bcrypt (pip install)

import sys
import psycopg2
import click
import os
import bcrypt


@click.group(chain=True)
@click.option('--database', default='vopro', help='PostgreSQL database name',
              prompt='Please enter the PostgreSQL database name')
@click.option('--user', default='vopro', help='PostgreSQL database user',
              prompt='Please enter the PostgreSQL database user')
@click.option('--password', default='vopro', help='PostgreSQL database password',
              prompt='Please enter the PostgreSQL database password')
@click.option('--host', default='localhost', help='PostgreSQL database host',
              prompt='Please enter the PostgreSQL database host')
@click.option('--port', default=5432, help='PostgreSQL database port',
              prompt='Please enter the PostgreSQL database port')
@click.pass_context
def cli(ctx, database, user, password, host, port):
    try:
        connection = psycopg2.connect(database=database, user=user, password=password, host=host, port=port)
        ctx.obj = {
            "connection": connection,
            "database": database,
            "username": user,
            "password": password,
            "host": host,
            "port": str(port)
        }

    except psycopg2.Error as e:
        print("Could not connect to the database: ")
        print(e.message)
        sys.exit(1)


@click.command(name='setup_db')
@click.option('--yes', is_flag=True, expose_value=False,
              prompt='Are you sure you want to (re)generate the database? \n'
              'This will remove any existing data.')
@click.pass_obj
def setup_db(obj):
    connection = obj['connection']

    with connection.cursor() as cursor:
        try:
            for ddl_file in sorted(os.listdir("./ddl")):
                if ddl_file.endswith(".sql"):
                    cursor.execute(open("./ddl/" + ddl_file, 'r').read())

            connection.commit()
        except psycopg2.Error as e:
            print("Executing one or more DDL files failed: ")
            print(e.pgerror)
            connection.rollback()
            sys.exit(1)


@click.command(name='add_admin')
@click.option('--email', help='E-mail address for the root account',
              prompt='Please enter an e-mail address for the root account')
@click.option('--firstname', help='First name for the root account',
              prompt='Please enter a first name for the root account')
@click.option('--lastname', help='Last name for the root account',
              prompt='Please enter a last name for the root account')
@click.option('--secret', help='Password for the root account',
              hide_input=True, confirmation_prompt=True,
              prompt='Please enter a secure password for the root account')
@click.pass_obj
def add_admin(obj, email, firstname, lastname, secret):
    encrypted_secret = bcrypt.hashpw(secret.encode('utf-8'), bcrypt.gensalt())
    connection = obj['connection']

    with connection.cursor() as cursor:
        try:
            query = "INSERT INTO person (first_name, last_name, email, password, is_admin) " \
                    "VALUES (%s, %s, %s, %s, %s)"
            cursor.execute(query, (firstname, lastname, email, encrypted_secret.decode('utf-8'), True))
            connection.commit()
        except psycopg2.Error as e:
            print("Adding admin account failed: ")
            print(e.pgerror)
            connection.rollback()
            sys.exit(1)


@click.command(name='context')
@click.pass_obj
def prod_context(obj):
    infile = open('./src/main/resources/application-context-example.xml')
    outfile = open('./src/main/resources/application-context.xml', 'w')

    replacements = {
        '{DB_HOST}': obj['host'],
        '{DB_PORT}': obj['port'],
        '{DB_NAME}': obj['database'],
        '{DB_USERNAME}': obj['username'],
        '{DB_PASSWORD}': obj['password']
    }

    for line in infile:
        for src, target in replacements.iteritems():
            line = line.replace(src, target)
        outfile.write(line)

    infile.close()
    outfile.close()


@click.command(name='test_context')
@click.option('--testdatabase', default='vopro_test', help='PostgreSQL database name',
              prompt='Please enter the PostgreSQL test database name')
@click.pass_obj
def test_context(obj, testdatabase):
    infile = open('./src/test/resources/spring_test_example.xml')
    outfile = open('./src/test/resources/spring_test.xml', 'w')

    replacements = {
        '{DB_HOST}': obj['host'],
        '{DB_PORT}': obj['port'],
        '{DB_NAME}': testdatabase,
        '{DB_USERNAME}': obj['username'],
        '{DB_PASSWORD}': obj['password']
    }

    for line in infile:
        for src, target in replacements.iteritems():
            line = line.replace(src, target)
        outfile.write(line)

    infile.close()
    outfile.close()


cli.add_command(setup_db)
cli.add_command(add_admin)
cli.add_command(prod_context)
cli.add_command(test_context)

if __name__ == '__main__':
    cli()
