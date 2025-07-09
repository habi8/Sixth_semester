import xmlrpc.server

def add_numbers(a, b):
    return a + b


server = xmlrpc.server.SimpleXMLRPCServer(("0.0.0.0", 8000))
print("Server running on port 8000...")

# Register the add_numbers function
server.register_function(add_numbers, "add")

# Start the server
server.serve_forever()
