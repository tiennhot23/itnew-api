
### FetchType
-   FetchType.LAZY (mặc định trong @OneToMany): khi select sẽ không select đối tượng liên quan
-   FetchType.EAGER (mặc định trong @ManyToOne): khi select sẽ select các dối tượng liên quan

### OrphanRemoval
Đánh dấu các phần tử con sẽ bị xoá khi nó bị xoá khỏi collection của phần tử cha

### 