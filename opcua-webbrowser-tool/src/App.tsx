import { defineComponent } from 'vue'; 
import './index.css';

export default defineComponent({
  name: 'App',
  setup() {
    return () => (
      <div class="app-containers">
        <router-view />
        {/* <Modeler />
        <Panel />
        <BpmnActions /> */}
      </div>
    );
  },
});
