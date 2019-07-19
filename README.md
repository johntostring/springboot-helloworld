# Istio example application
```yaml
kind: ConfigMap
apiVersion: v1
metadata:
  name: springboot-helloworld-production-v1
data:
  application.yml: |-
    spring:
      profiles: production
    example:
      color: blue
      version: v1.0-prod
---
kind: ConfigMap
apiVersion: v1
metadata:
  name: springboot-helloworld-production-v2
data:
  application.yml: |-
    spring:
      profiles: production
    example:
      color: green
      version: v2.0-prod
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: rbac-default-namespace
subjects:
  - kind: ServiceAccount
    name: default
    namespace: sinnet
roleRef:
  kind: ClusterRole
  name: cluster-admin
  apiGroup: rbac.authorization.k8s.io
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: springboot-helloworld-deploy-v1
  namespace: sinnet
  labels:
    app: springboot-helloworld-deploy
    version: v1
spec:
  selector:
    matchLabels:
      app: springboot-helloworld
      version: v1
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
#        sidecar/notinject: "on"
        app: springboot-helloworld
        version: v1
    spec:
      containers:
      - image: sinnet/springboot-helloworld:v1.0
        name: springboot-helloworld
        resources:
          requests:
            memory: "256Mi"
            cpu: "250m"
          limits:
            memory: "512Mi"
            cpu: "500m"
        env:
        - name: SPRING_CLOUD_KUBERNETES_CONFIG_NAME
          value: springboot-helloworld-production-v1
        - name: SPRING_PROFILES_ACTIVE
          value: production
        ports:
        - name: http
          containerPort: 8080
        readinessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 10
          timeoutSeconds: 2
          periodSeconds: 3
          failureThreshold: 5
        livenessProbe:
          httpGet:
            path: /health
            port: 8080
          initialDelaySeconds: 20
          timeoutSeconds: 2
          periodSeconds: 8
          failureThreshold: 5
---
apiVersion: v1
kind: Service
metadata:
  name: springboot-helloworld
  namespace: sinnet
  labels:
    app: springboot-helloworld
    service: springboot-helloworld
spec:
  type: ClusterIP
  externalIPs:
  - "192.168.201.88"
  ports:
  - port: 8080
  selector:
    app: springboot-helloworld
---
apiVersion: networking.istio.io/v1alpha3
kind: DestinationRule
metadata:
  name: springboot-helloworld
spec:
  host: springboot-helloworld.sinnet.svc.cluster.local
  subsets:
  - name: v1
    labels:
      version: v1
  - name: v2
    labels:
      version: v2
---
apiVersion: networking.istio.io/v1alpha3
kind: Gateway
metadata:
  name: springboot-helloworld-gateway
spec:
  selector:
    istio: ingressgateway
  servers:
    - port:
        number: 80
        name: http
        protocol: HTTP
      hosts:
      - hi.mykube.io
---
apiVersion: networking.istio.io/v1alpha3
kind: VirtualService
metadata:
  name: springboot-helloworld
spec:
  hosts:
    - hi.mykube.io
  gateways:
    - springboot-helloworld-gateway
  http:
  - route:
    - destination:
        host: springboot-helloworld.sinnet.svc.cluster.local
        subset: v1
      weight: 50
    - destination:
        host: springboot-helloworld.sinnet.svc.cluster.local
        subset: v2
      weight: 50

```