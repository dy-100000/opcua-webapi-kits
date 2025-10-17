declare module '*.vue' {
  import { defineComponent } from 'vue';
  const component: ReturnType<typeof defineComponent>;
  export default component;
}

declare module '*.graphql' {
  import { DocumentNode } from 'graphql';
  const schema: DocumentNode;
  export default schema;
}