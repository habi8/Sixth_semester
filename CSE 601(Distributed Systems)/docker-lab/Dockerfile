# Use Python as the base image
FROM python:3.9-slim

# Set working directory
WORKDIR /app

# Copy application file
COPY app.py .

# Expose the port the app runs on
EXPOSE 3000

# Command to run the application
CMD ["python", "app.py"]
