module.exports = (req, res, next) => {
  // Adicionar headers de resposta
  res.header('Content-Type', 'application/json');
  res.header('Access-Control-Allow-Origin', '*');
  res.header(
    'Access-Control-Allow-Headers',
    'Origin, X-Requested-With, Content-Type, Accept, Authorization'
  );
  res.header('Access-Control-Allow-Methods', 'GET, POST, PUT, DELETE, OPTIONS');

  // Responder a requisições OPTIONS
  if (req.method === 'OPTIONS') {
    return res.status(200).end();
  }

  // Validar token de autorização
  const authHeader = req.headers.authorization;
  const expectedToken = '6cJ1gA7c41An1TeVE13n19uenreb0azA';

  if (!authHeader || authHeader !== expectedToken) {
    console.log('Warning: Invalid or missing Authorization token');
    return res.status(401).json({ error: 'Unauthorized' });
  }

  // Simular delay de rede (reduzido para desenvolvimento)
  setTimeout(() => {
    next();
  }, 50);
};
