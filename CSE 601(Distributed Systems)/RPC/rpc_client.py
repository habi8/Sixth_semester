import xmlrpc.client


server = xmlrpc.client.ServerProxy("http://192.168.1.100:8000")

num1 = 10
num2 = 20

result = server.add(num1, num2)

print(f"The sum of {num1} and {num2} is: {result}")
