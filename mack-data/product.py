import psycopg2
from faker import Faker
from psycopg2 import extras

# ip address
host = '127.0.0.1'
database = 'fly_mall'
user = 'postgres'

batch = 10000
total = 10000000


def connect_db():
    return psycopg2.connect(host=host,
                            database=database,
                            user=user)


def close_db_connection(conn):
    conn.commit()
    conn.close()


if __name__ == '__main__':
    conn = connect_db()
    cursor = conn.cursor()

    fake = Faker('zh_CN')
    insertedNum = 0

    rowList = []
    while insertedNum < total:
        insertedNumNow = insertedNum
        currentBatch = 0
        while currentBatch < batch and insertedNumNow < total:
            row = [fake.text(max_nb_chars=30), fake.text(max_nb_chars=100)]
            rowMap = {
                'name': row[0],
                'description': row[1]
            }
            currentBatch += 1
            insertedNumNow += 1
            print("currentBatch: " + currentBatch.__str__())
            print("insertedNumNow: " + insertedNumNow.__str__())
            print(rowMap)
            rowList.append(row)

        sql = """
        insert into product(name,description) values %s
        """
        extras.execute_values(cursor, sql, rowList)
        conn.commit()

        insertedNum = insertedNumNow
        rowList.clear()
