from http.server import BaseHTTPRequestHandler, HTTPServer
import socket
import json

class WebServer(BaseHTTPRequestHandler):
    def _set_response(self, content_type="text/plain"):
        self.send_response(200)
        self.send_header('Content-type', content_type)
        self.end_headers()
        
    def do_GET(self):
        if self.path == '/':
            # Serve the main page as plain text
            self._set_response("text/plain")
            hostname = socket.gethostname()
            response = f"""
Hello from Docker!
This page is being served from a Docker container.
Container hostname: {hostname}
            """
            self.wfile.write(response.encode('utf-8'))
        elif self.path == '/api/info':
            # Serve JSON API response
            self._set_response("application/json")
            data = {
                "hostname": socket.gethostname(),
                "python_version": socket.python_version(),
                "container": True
            }
            self.wfile.write(json.dumps(data).encode('utf-8'))
        else:
            self._set_response()
            self.wfile.write(b"404 - Not Found")

def run(server_class=HTTPServer, handler_class=WebServer, port=3000):
    server_address = ('', port)
    httpd = server_class(server_address, handler_class)
    print(f"Starting server on port {port}...")
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    httpd.server_close()
    print("Server stopped.")

if __name__ == '__main__':
    run()
