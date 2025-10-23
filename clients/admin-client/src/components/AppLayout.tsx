import { AppBar, Toolbar, Typography, Box, Drawer, List, ListItemButton, ListItemText } from "@mui/material";
import { Link, Outlet } from "react-router-dom";

const drawerWidth = 240;

export default function AppLayout() {
  const menu = [
    { label: "Catalog", to: "/catalog" },
    { label: "Inventory", to: "/inventory" },
    { label: "Orders", to: "/orders" },
    { label: "Payments", to: "/payments" },
  ];

  return (
    <Box sx={{ display: "flex" }}>
      <AppBar position="fixed" sx={{ zIndex: 1201 }}>
        <Toolbar>
          <Typography variant="h6">E-Commerce Admin</Typography>
        </Toolbar>
      </AppBar>

      <Drawer
        variant="permanent"
        sx={{
          width: drawerWidth,
          [`& .MuiDrawer-paper`]: { width: drawerWidth, boxSizing: "border-box" },
        }}
      >
        <Toolbar />
        <List>
          {menu.map(item => (
            <ListItemButton key={item.to} component={Link} to={item.to}>
              <ListItemText primary={item.label} />
            </ListItemButton>
          ))}
        </List>
      </Drawer>

      <Box component="main" sx={{ flexGrow: 1, p: 3 }}>
        <Toolbar />
        <Outlet /> {/* child routes render here */}
      </Box>
    </Box>
  );
}
