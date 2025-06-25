curl http://100.82.106.113:11434/api/generate \
  -H "Content-Type: application/json" \
  -d '{
        "model": "phi3:mini",
        "prompt": "hello ollama? what is your model",
        "stream": false
      }'