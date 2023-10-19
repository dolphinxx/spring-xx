<template>
  <div></div>
  <header>
    <slot name="header">Default Header</slot>
  </header>
  <main>
    <slot class="foo" title="Foo">Default Main</slot>
  </main>
  <footer>
    <slot name="footer">Default Footer</slot>
  </footer>
  <footer>
    <slot name="trailing">Default Trailing</slot>
  </footer>
</template>
<script lang="ts" setup>

</script>
