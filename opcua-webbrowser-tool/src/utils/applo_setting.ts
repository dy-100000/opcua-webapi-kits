import { ApolloClient, createHttpLink, InMemoryCache } from '@apollo/client/core'
import { setContext } from "@apollo/client/link/context";

const httpLink = createHttpLink({
  uri: "http://172.26.18.29:4000", //你的GraphQL服务端接口地址
  // uri: "http://127.0.0.1:4000", 
  
});

const authLink = setContext((_, { headers }) => {
  const token = "linlin-authentication"; //根据你的应用需求，你可能需要在这里使用你自己的token

  return {
    headers: {
      ...headers,
      authorization: token ?  {token} : "",
    },
  };
});

const cache = new InMemoryCache();

export const apolloClient = new ApolloClient({
  link: authLink.concat(httpLink),
 cache,
});