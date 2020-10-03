WEBUI: http://localhost:8761/

REST:

http://localhost:8761/status

    {
      "applications": [
        {
          "name": "ZUUL-SERVICE",
          "instances": [
            {
              "status": "UP",
              "id": "macbook-pro:ZUUL-SERVICE:8762",
              "hostname": "192.168.2.178",
              "ip": "192.168.2.178",
              "port": 8762
            }
          ]
        }
      ]
    }

http://localhost:8761/status/ZUUL-SERVICE

    {
      "applications": [
        {
          "name": "ZUUL-SERVICE",
          "instances": [
            {
              "status": "UP",
              "id": "macbook-pro:ZUUL-SERVICE:8762",
              "hostname": "192.168.2.178",
              "ip": "192.168.2.178",
              "port": 8762
            }
          ]
        }
      ]
    }