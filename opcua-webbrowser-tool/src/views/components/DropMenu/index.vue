<template>
  <div
    class="dropdown-menu"
    v-show="visible"
    :style="{ left: left + 'px', top: top + 'px' }"
  >
    <slot></slot>
  </div>
</template>

<script>
export default {
  props: {
    visible: {
      type: Boolean,
      default: false,
    },
    left: {
      type: Number,
      default: 0,
    },
    top: {
      type: Number,
      default: 0,
    },
  },
  methods: {
    close() {
      this.$emit('update:visible', false);
    },
  },
};
</script>

<style scoped>
.dropdown-menu {
  position: absolute;
  background-color: #fff;
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.15);
  z-index: 1000;
  padding: 8px 0;
  min-width: 120px;
}
</style>